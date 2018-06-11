/*******************************************************************************
  * Copyright (c) 2007-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.vpe.editor.template;

import org.jboss.tools.vpe.editor.template.VpeTemplateManager.VpeTemplateContext;
import org.jboss.tools.vpe.editor.util.HTML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This abstract class is intended to provide a common way to implement templates
 * that have to render their contents into the {@code BODY}-element of the page.
 * 
 * In most cases only the method {@code #getTargetAttributeName(String)} should be
 * implemented.
 * 
 * @author ezheleznyakov@exadel.com
 * @author yradtsevich
 */
public abstract class VpeAbstractBodyTemplate extends VpeAbstractTemplate {
	
	/**
	 * This method should return target attribute name for the given 
	 * source attribute name.
	 *
	 * @return target attribute name, or {@code null} if the attribute
	 * have to be omitted.
	 */
	abstract protected String getTargetAttributeName(String sourceAttributeName);
	
	public VpeCreationData create(VpeTemplateContext context, Node sourceNode,
			Document visualDocument) {
		final Element body = getBody(visualDocument.getDocumentElement());
		cleanBody(body);

		final Element div = visualDocument.createElement(HTML.TAG_DIV);
		final NamedNodeMap sourceNodeAttributes = sourceNode.getAttributes();
		for (int i = 0; i < sourceNodeAttributes.getLength(); i++) {
			final Node sourceAttribute = sourceNodeAttributes.item(i);
			final String sourceAttributeName = sourceAttribute.getNodeName();
			String attributeValue = sourceAttribute.getNodeValue();
			final String targetAttributeName = getTargetAttributeName(sourceAttributeName);
			
			if (targetAttributeName != null) {
				if(HTML.ATTR_ID.equalsIgnoreCase(targetAttributeName)) {
					div.setAttribute(HTML.ATTR_ID, attributeValue);
				} else {
					if (HTML.ATTR_BACKGROUND.equalsIgnoreCase(targetAttributeName)
						|| HTML.ATTR_STYLE.equalsIgnoreCase(targetAttributeName)) {
						/*
						 * https://issues.jboss.org/browse/JBIDE-9975
						 * Simply set the background style, its correction will be made in 
						 * VpeVisualDomBuilder.correctVisualAttribute(..) method. 
						 * Wrong URLs won't be set by XULRunner itself.
						 */
						div.setAttribute(targetAttributeName, attributeValue);
					}
					body.setAttribute(targetAttributeName, attributeValue);
				}
			}
		}

		return new VpeCreationData(div);
	}

	/**
	 * Deletes all attributes except ID from the {@code body}
	 * 
	 * @param body BODY-element
	 */
	protected void cleanBody(final Element body) {
		NamedNodeMap attributes = body.getAttributes();

		long len = attributes.getLength();
		int j = 0;
		for (int i = 0; i < len; i++) {
			Node attr = attributes.item(j);
			if (HTML.ATTR_ID.equalsIgnoreCase(attr.getNodeName())) {
				j++;
			} else {
				body.removeAttribute(attr.getNodeName());
			}
		}
	}

	/**
	 * Finds {@code BODY}-element
	 * 
	 * @param node a visual node
	 * @return the nearest child of {@code node} named {@code 'BODY'}
	 */
	protected static Element getBody(Node node) {

		final NodeList nodeChildren = node.getChildNodes();
		for (int i = 0; i < nodeChildren.getLength(); i++) {
			final Node nodeChild = nodeChildren.item(i);
			if (HTML.TAG_BODY.equalsIgnoreCase(nodeChild
					.getNodeName())) {
				return (Element) nodeChild;
			} else {
				Element body = getBody(nodeChild);
				if (body != null) {
					return body;
				}
			}
		}

		return null;
	}
}
