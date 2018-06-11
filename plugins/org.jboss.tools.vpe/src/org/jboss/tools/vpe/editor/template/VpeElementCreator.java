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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager.VpeTemplateContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.jboss.tools.vpe.editor.template.expression.VpeValue;

public class VpeElementCreator extends VpeAbstractCreator {
	private boolean caseSensitive;
	private VpeExpression expression;

	VpeElementCreator(Element element, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		build(element, dependencyMap);
	}

	private void build(Element element, VpeDependencyMap dependencyMap) {
		Attr nameAttr = element.getAttributeNode(VpeTemplateManager.ATTR_ELEMENT_NAME);
		if (nameAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(nameAttr.getNodeValue(), caseSensitive);
				expression = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}
	}

	@Override
	public VpeCreatorInfo create(VpeTemplateContext context, Node sourceNode, Document visualDocument, Element visualElement, Map visualNodeMap) throws VpeExpressionException {
		if (expression != null) {
			visualNodeMap.put(this, visualElement);
			VpeValue vpeValue = expression.exec(context, sourceNode);
			if (vpeValue != null && vpeValue.stringValue().length() > 0) {
				Element newVisualElement = visualDocument.createElement(vpeValue.stringValue());
				return new VpeCreatorInfo(newVisualElement);
			}
		}
		return null;
	}
}
