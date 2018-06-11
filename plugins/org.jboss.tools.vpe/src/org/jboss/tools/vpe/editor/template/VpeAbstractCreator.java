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
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class VpeAbstractCreator implements VpeCreator {

	/**
	 * Creates a node of the visual tree on the node of the source tree.
	 * This visual node should not have the parent node
     * This visual node can have child nodes.
	 * @param pageContext Contains the information on edited page.
	 * @param sourceNode The current node of the source tree.
	 * @param visualDocument The document of the visual tree.
	 * @param visualElement The current element of the visual tree.
	 * @param visualNodeMap Is used for a storage padding information.
	 * @return The information on the created node of the visual tree. 
	 */
	public abstract VpeCreatorInfo create(VpeTemplateContext context, Node sourceNode, Document visualDocument, Element visualElement, Map visualNodeMap)throws VpeExpressionException ;
}
