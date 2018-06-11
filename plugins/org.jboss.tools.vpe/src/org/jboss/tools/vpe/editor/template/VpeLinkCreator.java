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
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager.VpeTemplateContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VpeLinkCreator extends VpeAbstractCreator {
	private boolean caseSensitive;
	private VpeExpression hrefExpr;

	VpeLinkCreator(Element taglibElement, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		build(taglibElement, dependencyMap);
	}

	private void build(Element element, VpeDependencyMap dependencyMap) {
		Attr hrefAttr = element.getAttributeNode(VpeTemplateManager.ATTR_LINK_HREF);
		if (hrefAttr != null) {
			try {
				String hrefStr = hrefAttr.getValue();
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(hrefStr, caseSensitive);
				hrefExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}
	}
	@Override
	public VpeCreatorInfo create(VpeTemplateContext context, Node sourceNode, Document visualDocument, Element visualElement, Map visualNodeMap) {
		String href_value = getExprValue(context, hrefExpr, sourceNode);

		Node newNode = context.getVisualBuilder().addLinkNodeToHead(href_value, "no", false); //$NON-NLS-1$
		visualNodeMap.put(this, newNode);
		VpeCreatorInfo creatorInfo = new VpeCreatorInfo(null);
		return creatorInfo;
	}

	private String getExprValue(VpeTemplateContext context, VpeExpression expr, Node sourceNode) {
		String value;
		if (expr != null) {
			try {
				value = expr.exec(context, sourceNode).stringValue();
			} catch (VpeExpressionException ex) {
					VpePlugin.reportProblem(ex);
					value=""; //$NON-NLS-1$
			}
		} else {
			value = ""; //$NON-NLS-1$
		}
		return value;
	}
}
