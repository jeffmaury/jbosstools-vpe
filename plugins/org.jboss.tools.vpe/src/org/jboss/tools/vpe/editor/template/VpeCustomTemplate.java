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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.jboss.tools.vpe.editor.VpeIncludeInfo;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager.VpeTemplateContext;
import org.jboss.tools.vpe.editor.template.custom.CustomTLDReference;
import org.jboss.tools.vpe.editor.template.custom.VpeCustomStringStorage;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author mareshkau
 * 
 */
public class VpeCustomTemplate extends VpeIncludeTemplate {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.vpe.editor.template.VpeIncludeTemplate#create(org.jboss
	 * .tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node,
	 * org.mozilla.interfaces.nsIDOMDocument)
	 */
	@Override
	public VpeCreationData create(VpeTemplateContext context, Node sourceNode,
			Document visualDocument) {

		IStorage sourceFileStorage = getCustomTemplateStorage(context, sourceNode);
	
		if (sourceFileStorage instanceof IFile) {
			//add attributes to EL list
			IFile file =(IFile) sourceFileStorage;
			if (file.exists()) {
				if (!context.isFileInIncludeStack(
						file)) {
					Document document = context.getIncludeDocuments().get(file);
					if (document == null) {
						document = VpeCreatorUtil.getDocumentForRead(file);
						if (document != null)
							context.getIncludeDocuments().put(file, document);
					}
					if (document != null) {
						return createCreationData(context, sourceNode, file, document, visualDocument);
					}
				}
			}
		}else if(sourceFileStorage instanceof VpeCustomStringStorage) {
			VpeCustomStringStorage customStringStorage = (VpeCustomStringStorage) sourceFileStorage;
			if (!context.isFileInIncludeStack(
					customStringStorage)) {
				Document document = context.getIncludeDocuments().get(customStringStorage);
				if (document == null) {
					document = VpeCreatorUtil.getDocumentForRead(customStringStorage.getContentString());
					if (document != null)
						context.getIncludeDocuments().put(customStringStorage, document);
				}
				if (document != null) {
					return createCreationData(context, sourceNode, customStringStorage, document, visualDocument);
				}
			}
		}
		VpeCreationData creationData = createStub(sourceNode.getNodeName(),
				visualDocument);
		creationData.setData(null);
		return creationData;
	}
	
	private VpeCreationData createCreationData(VpeTemplateContext context,Node sourceNode,
			IStorage storage,
			Document document, Document visualDocument) {
		VpeCreationData creationData = createInclude(
				document, visualDocument);

			addAttributesToELExcpressions(
										sourceNode, context);
				creationData.setData(storage);
		context.pushIncludeStack(
				new VpeIncludeInfo((Element) sourceNode,
						storage, document));
		return creationData;
	}
	
	/**
	 * Temparary add to attribute for custom el expressions
	 * @author mareshkau
	 * 
	 * @param pageContext Page Context
	 * @param sourceNode source Node
	 * @param processedFile processed File
	 * @return resourceReferences - unchanged resource references
	 */
	protected void addAttributesToELExcpressions(
			final Node sourceNode, final VpeTemplateContext context){
		NamedNodeMap attributesMap = sourceNode.getAttributes();

		for(int i=0;i<attributesMap.getLength();i++) {
			Attr attr = (Attr) attributesMap.item(i);
			context.addAttributeInCustomElementsMap(attr.getName(), attr.getValue());
		}
	}
	
	/**
	 * Culculate and returns storage to custom file
	 * @author mareshkau
	 * @param context
	 * @param sourceNode
	 * @return
	 */
	protected IStorage getCustomTemplateStorage(VpeTemplateContext context, Node sourceNode){
		return CustomTLDReference.getCustomElementStorage(sourceNode, context);
	}
}
