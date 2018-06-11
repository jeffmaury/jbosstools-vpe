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

import org.jboss.tools.jst.web.ui.internal.editor.preferences.VpePreference;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager.VpeTemplateContext;
import org.jboss.tools.vpe.editor.util.HTML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class VpeCommentCreator extends VpeAbstractCreator {
	public static final String SIGNATURE_VPE_COMMENT = ":vpe:comment"; //$NON-NLS-1$
	private static final String COMMENT_STYLE = "font-style:italic; color:green"; //$NON-NLS-1$
	private static final String COMMENT_PREFIX = ""; //$NON-NLS-1$
	private static final String COMMENT_SUFFIX = ""; //$NON-NLS-1$

	VpeCommentCreator(Element element, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		dependencyMap.setCreator(this, SIGNATURE_VPE_COMMENT);
	}

	@Override
	public VpeCreatorInfo create(VpeTemplateContext context, Node sourceNode, Document visualDocument, Element visualElement, Map visualNodeMap) {
		if(!"yes".equals(VpePreference.SHOW_COMMENTS.getValue())) { //$NON-NLS-1$
			return null;
		}
		Element div = visualDocument.createElement(HTML.TAG_DIV);
		div.setAttribute("style", COMMENT_STYLE); //$NON-NLS-1$
		String value = COMMENT_PREFIX + sourceNode.getNodeValue() + COMMENT_SUFFIX;
		Text text = visualDocument.createTextNode(value);
		div.appendChild(text);
		visualNodeMap.put(this, div);
		return new VpeCreatorInfo(div);
	}

}