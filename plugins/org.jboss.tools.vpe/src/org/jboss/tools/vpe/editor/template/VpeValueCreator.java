/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.template;

import java.util.Map;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager.VpeTemplateContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class VpeValueCreator extends VpeAbstractCreator {
	public static final String SIGNATURE_VPE_VALUE = ":vpe:value"; //$NON-NLS-1$

	private VpeExpression expression;
	private String outputAttrName;

	VpeValueCreator(String value, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		build(value, dependencyMap, caseSensitive);
	}
	
	private void build(String value, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		try {
			VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(value, caseSensitive);
			expression = info.getExpression();
			dependencyMap.setCreator(this, info.getDependencySet());
			outputAttrName = VpeExpressionBuilder.getOutputAttrName(value);
			if (outputAttrName != null) {
				dependencyMap.setCreator(this, SIGNATURE_VPE_VALUE);
			}
		} catch(VpeExpressionBuilderException e) {
			VpePlugin.reportProblem(e);
		}
	}

	@Override
	public VpeCreatorInfo create(VpeTemplateContext context, Node sourceNode, Document visualDocument, Element visualElement, Map visualNodeMap) throws VpeExpressionException {
		String value;
		if (expression != null) {
			value = expression.exec(context, sourceNode).stringValue();
		} else {
			value = ""; //$NON-NLS-1$
		}
		Text valueNode = visualDocument.createTextNode(value);
		visualNodeMap.put(this, valueNode);
		return new VpeCreatorInfo(valueNode);
	}
}
