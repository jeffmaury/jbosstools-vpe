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

/**
 * @author Gavrs
 *
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager.VpeTemplateContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.jboss.tools.vpe.editor.template.expression.VpeValue;
import org.jboss.tools.vpe.editor.util.HTML;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
public class VpeListCreator extends VpeAbstractCreator{
	
	
	private boolean caseSensitive;
	
	private List propertyCreators;
	private Set dependencySet;
	private VpeExpression layoutExpr;
	
	
	VpeListCreator(Element listElement, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		build(listElement, dependencyMap);
	}
	
	private void build(Element listElement, VpeDependencyMap dependencyMap) {
		
		Attr layoutAttr = listElement.getAttributeNode(VpeTemplateManager.ATTR_LIST_ORDERED);
		if (layoutAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(layoutAttr.getValue(), caseSensitive);
				layoutExpr = info.getExpression();
				dependencySet = info.getDependencySet();
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}
		
		

		if (VpeTemplateManager.ATTR_LIST_PROPERTIES != null) {
			for (int i = 0; i < VpeTemplateManager.ATTR_LIST_PROPERTIES.length; i++) {
				String attrName = VpeTemplateManager.ATTR_LIST_PROPERTIES[i];
				Attr attr = listElement.getAttributeNode(attrName);
				if (attr != null) {
					if (propertyCreators == null) propertyCreators  = new ArrayList();
					propertyCreators.add(new VpeAttributeCreator(attrName, attr.getValue(), dependencyMap, caseSensitive));
				}
			}
		}
	}
	
	@Override
	public VpeCreatorInfo create(VpeTemplateContext context, Node sourceNode, Document visualDocument, Element visualElement, Map visualNodeMap) throws VpeExpressionException {
		String strValue=null;
		int listSize = 0;
		if (layoutExpr != null) {
			VpeValue vpeValue = layoutExpr.exec(context, sourceNode);
			if (vpeValue != null) {
			 strValue = vpeValue.stringValue();
				
			}
		}
		
		Element visualList = visualDocument.createElement("true".equals(strValue)?HTML.TAG_OL:HTML.TAG_UL); //$NON-NLS-1$
		VpeCreatorInfo creatorInfo = new VpeCreatorInfo(visualList);

		for (int i = 0; i < propertyCreators.size(); i++) {
			VpeCreator creator = (VpeCreator)propertyCreators.get(i);
			if (creator != null) {
				VpeCreatorInfo info = creator.create(context, (Element) sourceNode, visualDocument, visualList, visualNodeMap);
				if (info != null && info.getVisualNode() != null) {
					Attr attr = (Attr)info.getVisualNode();
					visualList.setAttributeNode(attr);
				}
			}
		}
		
		NodeList children = sourceNode.getChildNodes();
		int count = children != null ? children.getLength() : 0;
		if (count > 0) {
			Node[] sourceChildren = new Node[count];
			int childrenCount = 0;
			for (int i = 0; i < count; i++) {
				Node node = children.item(i);
				int type = node.getNodeType();
				if (type == Node.ELEMENT_NODE || type == Node.TEXT_NODE && node.getNodeValue().trim().length() > 0) {
					sourceChildren[childrenCount] = node;
					childrenCount++;
				}
			}
			if (childrenCount > 0) {
				if (listSize == 0) {
					listSize = childrenCount;
				}
				
				for (int i = 0; i < listSize; i++) {
					Element visualLi = visualDocument.createElement(HTML.TAG_LI);
					
					
						VpeChildrenInfo childrenInfo = new VpeChildrenInfo(visualLi);
						childrenInfo.addSourceChild(sourceChildren[i]);
						creatorInfo.addChildrenInfo(childrenInfo);
						visualList.appendChild(visualLi);
					}
					
					
				
			}
		}
		creatorInfo.addDependencySet(dependencySet);
		return creatorInfo;
	}


}
