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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager.VpeTemplateContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.util.HTML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VpeHtmlTemplate extends VpeAbstractTemplate {
	public static final int TYPE_NONE = 0;
	public static final int TYPE_HTML = 1;
	public static final int TYPE_COPY = 2;
	public static final int TYPE_GRID = 3;
	public static final int TYPE_ANY = 4;
	public static final int TYPE_TAGLIB = 5;
	public static final int TYPE_LOAD_BUNDLE = 6;
	public static final int TYPE_DATATABLE = 7;
	public static final int TYPE_DATATABLE_COLUMN = 8;
	public static final int TYPE_COMMENT = 9;
	public static final int TYPE_STYLE = 10;
	public static final int TYPE_LINK = 11;
	//gavs
	public static final int TYPE_LIST = 12;
	public static final int TYPE_JSPROOT = 13;
	public static final int TYPE_LABELED_FORM = 13;
	
	public static final int TYPE_PANELGRID = 14;
	public static final int TYPE_FACET = 15;
	public static final int TYPE_INCLUDE = 16;
	public static final int PANEL_LAYOUT = 17;
	public static final int TYPE_A = 18;

	static final String ATTR_STYLE = "style"; //$NON-NLS-1$
	public static final String ATTR_STYLE_MODIFY_NAME = "-moz-user-modify"; //$NON-NLS-1$
	public static final String ATTR_STYLE_MODIFY_READ_WRITE_VALUE = "read-write"; //$NON-NLS-1$
	public static final String ATTR_STYLE_MODIFY_READ_ONLY_VALUE = "read-only"; //$NON-NLS-1$
	static final String ATTR_CURSOR_POINTER = "cursor:pointer;"; //$NON-NLS-1$

	private int type = TYPE_NONE;
	private VpeCreator creator = null;
	private VpeDependencyMap dependencyMap;
	private boolean dependencyFromBundle;
	@Override
	protected void init(Element templateElement) {
		dependencyMap = new VpeDependencyMap(caseSensitive);
		super.init(templateElement);
		dependencyMap.validate();
		dependencyFromBundle = dependencyMap.contains(VpeExpressionBuilder.SIGNATURE_JSF_VALUE);
	}
	@Override
	protected void initTemplateSection(Element templateSection) {
		if (creator == null) {
			String name = templateSection.getNodeName();
			if (name.startsWith(VpeTemplateManager.VPE_PREFIX)) {
				if (VpeTemplateManager.TAG_COPY.equals(name)) {
					type = TYPE_COPY;
					creator = new VpeCopyCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_ELEMENT.equals(name)) {
					type = TYPE_HTML;
					creator = new VpeElementCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_DATATABLE.equals(name)) {
					type = TYPE_DATATABLE;
					creator = new VpeDataTableCreator(templateSection, dependencyMap, caseSensitive);
				}
				else if (VpeTemplateManager.TAG_A.equals(name)) {
					type = TYPE_A;
					creator = new VpeVisualLinkCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_DATATABLE_COLUMN.equals(name)) {
					type = TYPE_DATATABLE_COLUMN;
					creator = new VpeDataTableColumnCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_FACET.equals(name)) {
					type = TYPE_FACET;
					creator = new VpeFacetCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_LIST.equals(name)) {
					type = TYPE_LIST;
					creator = new VpeListCreator(templateSection, dependencyMap, caseSensitive);
				// gavs	
				}	else if (VpeTemplateManager.TAG_LABELED_FORM.equals(name)) {
					type = TYPE_LABELED_FORM;
					creator = new VpeLabeledFormCreator(templateSection, dependencyMap, caseSensitive);
				// bela_n
				} else if (VpeTemplateManager.TAG_GRID.equals(name)) {
					type = TYPE_GRID;
					creator = new VpeGridCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_PANELGRID.equals(name)) {
					type = TYPE_PANELGRID;
					creator = new VpePanelGridCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_ANY.equals(name)) {
					type = TYPE_ANY;
					creator = new VpeAnyCreator(templateSection, dependencyMap, caseSensitive);
				} 
//				else if (VpeTemplateManager.TAG_TAGLIB.equals(name)) {
//					type = TYPE_TAGLIB;
//					creator = new VpeTaglibCreator(templateSection, dependencyMap);
//				}
				else if (VpeTemplateManager.TAG_LINK.equals(name)) {
					type = TYPE_LINK;
					creator = new VpeLinkCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_LOAD_BUNDLE.equals(name)) {
					type = TYPE_LOAD_BUNDLE;
					creator = new VpeLoadBundleCreator(templateSection, dependencyMap);
				} else if (VpeTemplateManager.TAG_COMMENT.equals(name)) {
					type = TYPE_COMMENT;
					creator = new VpeCommentCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_STYLE.equals(name)) {
					type = TYPE_STYLE;
					creator = new VpeStyleCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_JSPROOT.equals(name)) {
					type = TYPE_JSPROOT;
					creator = new VpeJspRootCreator(templateSection, dependencyMap);
				} else if (VpeTemplateManager.TAG_MY_FACES_PAGE_LAYOUT.equals(name)) {
					type = PANEL_LAYOUT;
					creator = new VpePanelLayoutCreator(templateSection, dependencyMap, caseSensitive);
				}
			} else {
				type = TYPE_HTML;
				creator = new VpeHtmlCreator(templateSection, dependencyMap, caseSensitive);
			}
		}
	}
	
	@Override
	public VpeCreationData create(VpeTemplateContext context, Node sourceNode, Document visualDocument) {
		Map<VpeTemplate, ModifyInfo> visualNodeMap = new HashMap<VpeTemplate, ModifyInfo> ();
		VpeCreatorInfo creatorInfo = null;
		if (sourceNode instanceof Element) {
			creatorInfo = createVisualElement(context, (Element)sourceNode, visualDocument, null, visualNodeMap);
		}
		Element newVisualElement = null;
		if (creatorInfo != null) {
			newVisualElement = (Element)creatorInfo.getVisualNode();
		}
		VpeCreationData creationData = new VpeCreationData(newVisualElement);
		if (creatorInfo != null) {
			List<VpeChildrenInfo> childrenInfoList = creatorInfo.getChildrenInfoList();
			if (childrenInfoList != null) {
				for (int i = 0; i < childrenInfoList.size(); i++) {
					creationData.addChildrenInfo((VpeChildrenInfo)childrenInfoList.get(i));
				}
			}
		}
		creationData.setData(visualNodeMap);
		return creationData;
	}

	private VpeCreatorInfo createVisualElement(VpeTemplateContext context, Element sourceElement, Document visualDocument, Element visualParent, Map<VpeTemplate,ModifyInfo> visualNodeMap) {
		if (creator == null) {
			return null;
		}
		VpeCreatorInfo elementInfo =null;
		try {
			elementInfo = creator.create(context, sourceElement, visualDocument, visualParent, visualNodeMap);
		} catch(VpeExpressionException ex) {
			
			VpeExpressionException exception = new VpeExpressionException("Exception on processing node "+sourceElement.toString(), ex); //$NON-NLS-1$
			VpePlugin.reportProblem(exception);
		}
		if (elementInfo != null) {
			Element visualElement = (Element)elementInfo.getVisualNode();
			if (visualElement != null) {
				boolean curModify = getCurrentModify(sourceElement, visualNodeMap);
				makeModify(visualElement, curModify);
				if (!HTML.TAG_INPUT.equalsIgnoreCase(visualElement.getNodeName()) && dependencyFromBundle && !curModify) {
					String style = visualElement.getAttribute(ATTR_STYLE);
					visualElement.setAttribute(ATTR_STYLE, style + ATTR_CURSOR_POINTER);
				}
				visualNodeMap.put(this, new ModifyInfo(visualElement, curModify));
			}
		}
		return elementInfo;
	}


	/**
	 * 
	 * Deprecated
	 */
	@Override
	public int getType() {
		return type;
	}

	/**
	 * 
	 * Deprecated
	 */
	@Override
	public VpeAnyData getAnyData() {
		VpeAnyData data = null;
		if (getType() == TYPE_ANY && creator != null) {
			data = ((VpeAnyCreator)creator).getAnyData();
			data.setCaseSensitive(caseSensitive);
			data.setChildren(children);
			data.setModify(modify);
		}
		return data;
	}
	
	/**
	 * 
	 * Deprecated
	 */
	public boolean isDependencyFromBundle() {
		return dependencyFromBundle;
	}
	

	private static final String VIEW_TAGNAME = "view"; //$NON-NLS-1$
	private static final String LOCALE_ATTRNAME = "locale"; //$NON-NLS-1$
	private static final String PREFIX_SEPARATOR = ":"; //$NON-NLS-1$

	
	static void makeModify(Element visualElement, boolean modify) {
		String s = ATTR_STYLE_MODIFY_NAME + ":" +  //$NON-NLS-1$
				(modify ? ATTR_STYLE_MODIFY_READ_WRITE_VALUE : ATTR_STYLE_MODIFY_READ_ONLY_VALUE);
		String style = visualElement.getAttribute(ATTR_STYLE);
		if (style != null && style.length() > 0) {
			if (style.indexOf(ATTR_STYLE_MODIFY_NAME) >= 0) {
				String[] items =  style.split(";"); //$NON-NLS-1$
				style = ""; //$NON-NLS-1$
				for (int i = 0; i < items.length; i++) {
					String[] item = items[i].split(":"); //$NON-NLS-1$
					if (ATTR_STYLE_MODIFY_NAME.trim().equalsIgnoreCase(item[0].trim())) {
						item[1] = modify ? ATTR_STYLE_MODIFY_READ_WRITE_VALUE : ATTR_STYLE_MODIFY_READ_ONLY_VALUE;
					}
					style += item[0] + ":" + item[1] + ";"; //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else {
				if (!";".equals(style.substring(style.length() - 1)))  style += ";"; //$NON-NLS-1$ //$NON-NLS-2$
				style += s;
			}
		} else {
			style = s;
		}
		visualElement.setAttribute(ATTR_STYLE, style);
	}
	
	private boolean getCurrentModify(Element sourceElement, Map visualNodeMap) {
		if (!modify || !dependencyFromBundle) {
			return modify;
		}
		VpeCreator[] creators = dependencyMap.getCreators(VpeValueCreator.SIGNATURE_VPE_VALUE);
		for (int i = 0; i < creators.length; i++) {
			if (creators[i] instanceof VpeOutputAttributes) {
				boolean currModify = ((VpeOutputAttributes)creators[i]).isEditabledAtribute(sourceElement, visualNodeMap);
				if (!currModify) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	private static class ModifyInfo {
		private Element visualElement;
		private boolean modify;
		
		private ModifyInfo(Element visualElement, boolean modify) {
			this.visualElement = visualElement;
			this.modify = modify;
		}
	}

	
}