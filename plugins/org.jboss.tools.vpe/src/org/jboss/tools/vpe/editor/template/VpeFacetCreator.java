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

import org.jboss.tools.vpe.editor.template.VpeTemplateManager.VpeTemplateContext;
import org.jboss.tools.vpe.editor.util.HTML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VpeFacetCreator extends VpeAbstractCreator {


	VpeFacetCreator(Element element, VpeDependencyMap dependencyMap, boolean caseSensitive) {
	}

	@Override
	public VpeCreatorInfo create(VpeTemplateContext context, Node sourceNode, Document visualDocument, Element visualElement, Map visualNodeMap) {
		VpeCreatorInfo creatorInfo = null;
		
		boolean isHeader = false;
		boolean isFooter = false;

		Node nameAttr = sourceNode.getAttributes().getNamedItem("name"); //$NON-NLS-1$
		if (nameAttr != null) {
			String name = nameAttr.getNodeValue();
			isHeader = name.equals("header"); //$NON-NLS-1$
			isFooter = name.equals("footer"); //$NON-NLS-1$
		}

		if (isHeader || isFooter) {
			Node sourceParent = sourceNode.getParentNode();
			if (sourceParent != null) {

				Element cell = null;
				int columnsCount = getColumnsCount(sourceParent);
				if (isHeader) {
					cell = makeCell(columnsCount, HTML.TAG_TH, visualDocument);
				} else if (isFooter) {
					cell = makeCell(columnsCount, HTML.TAG_TD, visualDocument);
				}
				if (cell != null) {
					if (isHeader) {
						setCellClass(cell, getTableAttrValue(sourceParent, "headerClass")); //$NON-NLS-1$
					} else if (isFooter) {
						setCellClass(cell, getTableAttrValue(sourceParent, "footerClass")); //$NON-NLS-1$
					}
					creatorInfo = new VpeCreatorInfo(cell);
				}
			}
		}
		return creatorInfo;
	}

	private void setCellClass(Element cell, String className) {
		if (cell != null) {
			if (className != null && className.trim().length() > 0) {
				cell.setAttribute("class", className); //$NON-NLS-1$
			}
		}
	}

	private String getTableAttrValue(Node dataTableNode, String attrName) {
		if (dataTableNode != null) {
			Node attr = dataTableNode.getAttributes().getNamedItem(attrName);
			if (attr != null) {
				return attr.getNodeValue();
			}
		}
		return null;
	}

	private int getColumnsCount(Node dataTableNode) {
		int count = 0;
		NodeList childs = dataTableNode.getChildNodes();
		int length = childs != null ? childs.getLength() : 0;
		for (int i = 0; i < length; i++) {
			Node child = childs.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().indexOf(":column") > 0) { //$NON-NLS-1$
				count++;
			}
		}
		return count;
	}

	private Element makeCell(int columnCount, String cellTag, Document visualDocument) {
		Element visualCell = visualDocument.createElement(cellTag);
		if (columnCount > 1) {
			visualCell.setAttribute("colspan", "" + columnCount); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return visualCell;
	}
}
