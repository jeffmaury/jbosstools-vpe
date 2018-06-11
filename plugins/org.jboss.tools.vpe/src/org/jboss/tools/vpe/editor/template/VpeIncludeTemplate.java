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

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.jboss.tools.jst.web.ui.internal.editor.util.NodesManagingUtil;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeIncludeInfo;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager.VpeTemplateContext;
import org.jboss.tools.vpe.editor.template.expression.VpeAttributeOperand;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.template.expression.VpeValue;
import org.jboss.tools.vpe.editor.util.FaceletUtil;
import org.jboss.tools.vpe.editor.util.FileUtil;
import org.jboss.tools.vpe.editor.util.HTML;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VpeIncludeTemplate extends VpeAbstractTemplate {
	private static final String ATTR_FILE = "file"; //$NON-NLS-1$
	private VpeExpression fileNameExpression;
	
	@Override
	protected void init(Element templateElement) {
		modify = false;
		Attr fileAttr = ((Element)templateElement).getAttributeNode(ATTR_FILE);
		if (fileAttr != null && fileAttr.getValue().trim().length() > 0) {
			try {
				fileNameExpression = VpeExpressionBuilder.buildCompletedExpression(fileAttr.getValue().trim(), caseSensitive).getExpression();
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}
		initTemplateSections(templateElement, false, true, false, false, false);
	}

	public VpeCreationData create(VpeTemplateContext context, Node sourceNode, Document visualDocument) {
		String fileName = null;
		if (fileNameExpression != null) {
			VpeValue vpeValue;
			try {
				vpeValue = fileNameExpression.exec(context, sourceNode);
			if (vpeValue != null && vpeValue.stringValue().length() > 0) {
				fileName = vpeValue.stringValue();
				VpeIncludeInfo info = context.getCurrentIncludeInfo();
				if(info != null && info.getStorage() instanceof IFile) {
					IFile templateFile = (IFile) info.getStorage();
					IFile file = FileUtil.getFile(fileName, templateFile);
					
					if (file != null) {
						if (!context.isFileInIncludeStack(file)) {
							Document document = context.getIncludeDocuments().get(file);
							if (document == null) {
								document = VpeCreatorUtil.getDocumentForRead(file);
								if (document != null) {
									context.getIncludeDocuments().put(file, document);
								}
							}
							if (document != null) {
								VpeCreationData creationData = createInclude(context, document, visualDocument);
								creationData.setData(file);
								context.pushIncludeStack(new VpeIncludeInfo((Element)sourceNode, file, document));
								return creationData;
							}
						}
					}
				}
			}
			} catch (VpeExpressionException e) {
				VpeExpressionException exception = new VpeExpressionException(
						sourceNode.toString(),e);
				VpePlugin.reportProblem(exception);
			}
		}
		
		VpeCreationData creationData = createStub(context, fileName, visualDocument);
		creationData.setData(null);
		return creationData;
	}

	protected VpeCreationData createInclude(VpeTemplateContext context, Document sourceDocument, Document visualDocument) {
		Element visualNewElement = visualDocument.createElement(HTML.TAG_DIV);
		context.markIncludeElement(visualNewElement);
		VpeCreationData creationData = new VpeCreationData(visualNewElement);
		if (children) {
			VpeChildrenInfo childrenInfo = new VpeChildrenInfo(visualNewElement);
			Element root = FaceletUtil.findComponentElement(sourceDocument.getDocumentElement());
			NodeList sourceChildren=null;
			//fix for JBIDE-3482
			if(root==null) {
				sourceChildren = sourceDocument.getChildNodes();
			} else {
				sourceChildren = root.getChildNodes();
			}
			int len = sourceChildren.getLength();
			for (int i = 0; i < len; i++) {
				childrenInfo.addSourceChild(sourceChildren.item(i));
			}
			creationData.addChildrenInfo(childrenInfo);
		}
		return creationData;
	}
	
	protected VpeCreationData createStub(VpeTemplateContext context, String fileName, Document visualDocument) {
		Element visualNewElement = visualDocument.createElement(HTML.TAG_DIV);
		visualNewElement.setAttribute("style", "background-color:#ECF3FF;cursor:pointer;padding:0 5px;margin:3px 0;font-style:italic;color:#0051DD;"); //$NON-NLS-1$ //$NON-NLS-2$
		context.markIncludeElement(visualNewElement);
		if (fileName != null) {
			visualNewElement.appendChild(visualDocument.createTextNode(fileName));
		}
		return new VpeCreationData(visualNewElement);
	}
}
