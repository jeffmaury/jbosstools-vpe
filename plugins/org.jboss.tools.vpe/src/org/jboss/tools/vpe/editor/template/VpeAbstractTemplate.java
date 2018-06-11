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
import java.util.HashSet;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager.VpeTemplateContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.template.resize.VpeResizer;
import org.jboss.tools.vpe.editor.template.textformating.TextFormatingData;
import org.jboss.tools.vpe.editor.util.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// TODO: Auto-generated Javadoc
/**
 * Class which response for configuration template element from configuration
 * file.
 */
public abstract class VpeAbstractTemplate implements VpeTemplate {


    /** The case sensitive. */
    protected boolean caseSensitive;
	
	/** The children. */
	protected boolean children;
	
	/** The modify. */
	protected boolean modify;

	/** The has imaginary border. */
	protected boolean hasImaginaryBorder;
	
	/** the invisible */
	protected boolean invisible;

	/** a resizer instance. */
	private VpeResizer resizer;

	/** The text formating data. */
	private TextFormatingData textFormatingData;
	
	/** The pseudo content creator. */
	private VpePseudoContentCreator pseudoContentCreator;
	
	/** The priority to load templates for the tag */
	protected double priority = 0.0;

	/** The Constant TAG_BREAKER. */
	private static final String TAG_BREAKER = VpeTemplateManager.VPE_PREFIX
			+ "breaker"; //$NON-NLS-1$
	
	/** The Constant ATTR_BREAKER_TYPE. */
	private static final String ATTR_BREAKER_TYPE = "type"; //$NON-NLS-1$
	
	/** The Constant ATTR_BREAKER_TYPE_IGNORE. */
	private static final String ATTR_BREAKER_TYPE_IGNORE = "ignore"; //$NON-NLS-1$
	
	/** The Constant ATTR_BREAKER_TYPE_SELECTITEM. */
	private static final String ATTR_BREAKER_TYPE_SELECTITEM = "selectItem"; //$NON-NLS-1$
	
	/** The Constant BREAKER_TYPE_NONE. */
	private static final int BREAKER_TYPE_NONE = 0;
	
	/** The Constant BREAKER_TYPE_IGNORE. */
	private static final int BREAKER_TYPE_IGNORE = 1;
	
	/** The Constant BREAKER_TYPE_SELECTITEM. */
	private static final int BREAKER_TYPE_SELECTITEM = 2;

	/** The Constant TAG_PSEUDOCONTENT. */
	private static final String TAG_PSEUDOCONTENT = VpeTemplateManager.VPE_PREFIX
			+ "pseudoContent"; //$NON-NLS-1$
	
	/** The Constant ATTR_PSEUDOCONTENT_DEFAULTTEXT. */
	private static final String ATTR_PSEUDOCONTENT_DEFAULTTEXT = "defaultText"; //$NON-NLS-1$
	
	/** The Constant ATTR_PSEUDOCONTENT_ATTRNAME. */
	private static final String ATTR_PSEUDOCONTENT_ATTRNAME = "attrName"; //$NON-NLS-1$

	protected static final String[] EMPTY_ARRAY = new String[0];;

	/** The breaker type. */
	private int breakerType = BREAKER_TYPE_NONE;

	/** The inline tags. */
	static private HashSet<String> inlineTags = new HashSet<String>();
	static {
		inlineTags.add("b"); //$NON-NLS-1$
		inlineTags.add("i"); //$NON-NLS-1$
		inlineTags.add("u"); //$NON-NLS-1$
		inlineTags.add("img"); //$NON-NLS-1$ 
		inlineTags.add("sub"); //$NON-NLS-1$
		inlineTags.add("sup"); //$NON-NLS-1$
		inlineTags.add("strike"); //$NON-NLS-1$
		inlineTags.add("font"); //$NON-NLS-1$
		inlineTags.add("a"); //$NON-NLS-1$
		inlineTags.add("input"); //$NON-NLS-1$
		inlineTags.add("textarea"); //$NON-NLS-1$
		inlineTags.add("span"); //$NON-NLS-1$
		inlineTags.add("button"); //$NON-NLS-1$
		inlineTags.add("label"); //$NON-NLS-1$
	}
	
	/** The tag resize constrans. */
	static private HashMap<String, Integer> tagResizeConstrans = new HashMap<String, Integer>();
	static {
		tagResizeConstrans
				.put(
						"table", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_ALL)); //$NON-NLS-1$
		tagResizeConstrans.put(
				"tr", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_ALL)); //$NON-NLS-1$
		tagResizeConstrans
				.put(
						"br", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_NONE)); //$NON-NLS-1$
		tagResizeConstrans.put(
				"b", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_NONE)); //$NON-NLS-1$
		tagResizeConstrans.put(
				"i", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_NONE)); //$NON-NLS-1$
		tagResizeConstrans.put(
				"u", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_NONE)); //$NON-NLS-1$
		tagResizeConstrans
				.put(
						"sub", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_NONE)); //$NON-NLS-1$
		tagResizeConstrans
				.put(
						"sup", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_NONE)); //$NON-NLS-1$
		tagResizeConstrans
				.put(
						"strike", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_NONE)); //$NON-NLS-1$
		tagResizeConstrans
				.put(
						"font", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_NONE)); //$NON-NLS-1$
		tagResizeConstrans.put(
				"a", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_NONE)); //$NON-NLS-1$
	}

	/** The break with paragraph tags. */
	static private HashSet<String> breakWithParagraphTags = new HashSet<String>();
	static {
		breakWithParagraphTags.add("b"); //$NON-NLS-1$
		breakWithParagraphTags.add("a"); //$NON-NLS-1$
		breakWithParagraphTags.add("abbr"); //$NON-NLS-1$
		breakWithParagraphTags.add("acronym"); //$NON-NLS-1$
		breakWithParagraphTags.add("b"); //$NON-NLS-1$
		breakWithParagraphTags.add("bdo"); //$NON-NLS-1$
		breakWithParagraphTags.add("big"); //$NON-NLS-1$
		breakWithParagraphTags.add("blink"); //$NON-NLS-1$
		breakWithParagraphTags.add("cite"); //$NON-NLS-1$
		breakWithParagraphTags.add("code"); //$NON-NLS-1$
		breakWithParagraphTags.add("del"); //$NON-NLS-1$
		breakWithParagraphTags.add("dfn"); //$NON-NLS-1$
		breakWithParagraphTags.add("em"); //$NON-NLS-1$
		breakWithParagraphTags.add("font"); //$NON-NLS-1$
		breakWithParagraphTags.add("ins"); //$NON-NLS-1$
		breakWithParagraphTags.add("kbd"); //$NON-NLS-1$
		breakWithParagraphTags.add("nobr"); //$NON-NLS-1$
		breakWithParagraphTags.add("q"); //$NON-NLS-1$
		breakWithParagraphTags.add("s"); //$NON-NLS-1$
		breakWithParagraphTags.add("samp"); //$NON-NLS-1$
		breakWithParagraphTags.add("small"); //$NON-NLS-1$
		breakWithParagraphTags.add("span"); //$NON-NLS-1$
		breakWithParagraphTags.add("strike"); //$NON-NLS-1$
		breakWithParagraphTags.add("strong"); //$NON-NLS-1$
		breakWithParagraphTags.add("tt"); //$NON-NLS-1$
		breakWithParagraphTags.add("u"); //$NON-NLS-1$
		breakWithParagraphTags.add("var"); //$NON-NLS-1$
	}
	
	/** The break without paragraph tags. */
	static private HashSet<String> breakWithoutParagraphTags = new HashSet<String>();
	static {
		breakWithoutParagraphTags.add("p"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("address"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("blockquote"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("center"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("div"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("h1"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("h2"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("h3"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("h4"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("h5"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("h6"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("p"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("pre"); //$NON-NLS-1$
	}

	/**
	 * Initiates template after its creating.
	 * 
	 * @param caseSensitive The case sensitive of an element of a source file
	 * @param templateElement <code>Element</code> with a name "vpe:template" from the
	 * template file
	 */
	public void init(Element templateElement, boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		children = Constants.YES_STRING.equals(templateElement.getAttribute(VpeTemplateManager.ATTR_TEMPLATE_CHILDREN));
		modify = Constants.YES_STRING.equals(templateElement.getAttribute(VpeTemplateManager.ATTR_TEMPLATE_MODIFY));
		invisible = Constants.YES_STRING.equals(templateElement.getAttribute(VpeTemplateManager.ATTR_TEMPLATE_INVISIBLE));

		String strHasImaginaryBorder = templateElement
				.getAttribute(VpeTemplateManager.ATTR_TEMPLATE_HAS_IMAGINARY_BORDER);

		if (strHasImaginaryBorder != null
				&& strHasImaginaryBorder.length() != 0) {
			hasImaginaryBorder = Constants.YES_STRING.equalsIgnoreCase(strHasImaginaryBorder);
		} else {
			hasImaginaryBorder = false;
		}

		init(templateElement);
	}

	/**
	 * Init.
	 * 
	 * @param templateElement the template element
	 */
	protected void init(Element templateElement) {
		initTemplateSections(templateElement, true, true, true, true, true);
	}

	/**
	 * Inits the template sections.
	 * 
	 * @param templateElement the template element
	 * @param breakHandler the break handler
	 * @param textFormatingHandler the text formating handler
	 * @param pseudoContentHandler the pseudo content handler
	 * @param resizeHandler the resize handler
	 * @param dndHandler the dnd handler
	 */
	protected void initTemplateSections(Element templateElement,
			boolean resizeHandler, boolean dndHandler,
			boolean textFormatingHandler, boolean breakHandler,
			boolean pseudoContentHandler) {
		NodeList children = templateElement.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				Node section = children.item(i);
				if (section.getNodeType() == Node.ELEMENT_NODE) {
					String sectionName = section.getNodeName();
					if (resizeHandler
							&& VpeTemplateManager.TAG_RESIZE
									.equals(sectionName)) {
						initResizeHandler((Element) section);
					} else if (textFormatingHandler
							&& VpeTemplateManager.TAG_TEXT_FORMATING
									.equals(sectionName)) {
						initTextFormatingHandler((Element) section);
					} else if (breakHandler && TAG_BREAKER.equals(sectionName)) {
						initBreakHandler((Element) section);
					} else if (pseudoContentHandler
							&& TAG_PSEUDOCONTENT.equals(sectionName)) {
						initPseudoContentHandler((Element) section);
					} else {
						initTemplateSection((Element) section);
					}
				}
			}
		}
	}

	/**
	 * Inits the resize handler.
	 * 
	 * @param templateSection the template section
	 */
	private void initResizeHandler(Element templateSection) {
		if (resizer == null) {
			resizer = new VpeResizer();
			resizer.setResizeData(templateSection);
		}
	}

	/**
	 * Inits the text formating handler.
	 * 
	 * @param templateSection the template section
	 */
	private void initTextFormatingHandler(Element templateSection) {
		if (textFormatingData == null) {
			textFormatingData = new TextFormatingData(templateSection);
		}
	}

	/**
	 * Inits the break handler.
	 * 
	 * @param templateSection the template section
	 */
	private void initBreakHandler(Element templateSection) {
		if (breakerType == BREAKER_TYPE_NONE) {			
			if (templateSection.hasAttribute(ATTR_BREAKER_TYPE)) {
				String typeValue = templateSection.getAttribute(ATTR_BREAKER_TYPE);
				if (ATTR_BREAKER_TYPE_IGNORE.equalsIgnoreCase(typeValue)) {
					breakerType = BREAKER_TYPE_IGNORE;
				} else if (ATTR_BREAKER_TYPE_SELECTITEM
						.equalsIgnoreCase(typeValue)) {
					breakerType = BREAKER_TYPE_SELECTITEM;
				}
			}
		}
	}

	/**
	 * Inits the pseudo content handler.
	 * 
	 * @param templateSection the template section
	 */
	private void initPseudoContentHandler(Element templateSection) {
		if (pseudoContentCreator == null) {
			if ("yes".equalsIgnoreCase(templateSection.getAttribute(ATTR_PSEUDOCONTENT_DEFAULTTEXT))) { //$NON-NLS-1$
				pseudoContentCreator = new VpeTextPseudoContentCreator(null,
						templateSection
								.getAttribute(ATTR_PSEUDOCONTENT_ATTRNAME));
			} else {
				NodeList children = templateSection.getChildNodes();
				if (children != null) {
					int len = children.getLength();
					for (int i = 0; i < len; i++) {
						Node child = children.item(i);
						if (child.getNodeType() == Node.ELEMENT_NODE) {
							pseudoContentCreator = new VpeHtmlPseudoContentCreator(
									(Element) child);
							return;
						}
					}
					for (int i = 0; i < len; i++) {
						Node child = children.item(i);
						if (child.getNodeType() == Node.TEXT_NODE) {
							String text = child.getNodeValue().trim();
							if (text.length() > 0) {
								pseudoContentCreator = new VpeTextPseudoContentCreator(
										text, null);
								return;
							}
						}
					}
				}
				pseudoContentCreator = VpeEmptyPseudoContentCreator
						.getInstance();
			}
		}
	}

	/**
	 * Inits the template section.
	 * 
	 * @param templateSection the template section
	 */
	protected void initTemplateSection(Element templateSection) {
	}

	/** The f current region to format. */
	IRegion fCurrentRegionToFormat = null;

	/**
	 * Gets the region to format.
	 * 
	 * @return the region to format
	 */
	private IRegion getRegionToFormat() {
		return fCurrentRegionToFormat;
	}

	/**
	 * Clear region to format.
	 */
	private void clearRegionToFormat() {
		fCurrentRegionToFormat = null;
	}

	/**
	 * Update region to format.
	 * 
	 * @param node the node
	 */
	private void updateRegionToFormat(Node node) {
		if (node instanceof IndexedRegion) {
			IndexedRegion region = (IndexedRegion) node;
			int start = region.getStartOffset();
			int end = region.getEndOffset();

			if (fCurrentRegionToFormat == null) {
				fCurrentRegionToFormat = new Region(start, end - start);
			} else {
				int cStart = fCurrentRegionToFormat.getOffset();
				int cEnd = fCurrentRegionToFormat.getOffset()
						+ fCurrentRegionToFormat.getLength();

				if (start < cStart)
					cStart = start;
				if (end > cEnd)
					cEnd = end;

				fCurrentRegionToFormat = new Region(cStart, cEnd - cStart);
			}
		}
	}

	/**
	 * Delete from string.
	 * 
	 * @param end the end
	 * @param data the data
	 * @param begin the begin
	 * 
	 * @return the string
	 */
	protected String deleteFromString(String data, String begin, String end) {
		int startPosition = data.indexOf(begin);

		if (startPosition < 0)
			return data;

		int endPosition = data.indexOf(end, startPosition);

		String result = data.substring(0, startPosition).trim();
		if (endPosition > 0) {
			result += data.substring(endPosition + 1, data.length()).trim();
		}

		return result;
	}

	/**
	 * Returns the data for formatting an element of source tree.
	 * 
	 * @return <code>TextFormatingData</code>
	 */
	public TextFormatingData getTextFormattingData() {
		return textFormatingData;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 * 
	 * @deprecated
	 */
	public int getType() {
		return 0;
	}

	/**
	 * Gets the any data.
	 * 
	 * @return the any data
	 * 
	 * @deprecated
	 */
	public VpeAnyData getAnyData() {
		return null;
	}

//	/**
//	 * Opens editor of source file for include-element.
//	 * 
//	 * @param sourceElement The current element of the source tree.
//	 * @param data The arbitrary data, built by a method <code>create</code>
//	 * @param pageContext Contains the information on edited page.
//	 */
//	public void openIncludeEditor(VpePageContext pageContext,
//			Element sourceElement, Object data) {
//	}

	/**
	 * The unfilled element of an source tree can be mapped in the visiblis
	 * editor with the default contents This method fills default contents.
	 * 
	 * @param visualDocument The document of the visual tree.
	 * @param pageContext Contains the information on edited page.
	 * @param sourceContainer The current element of the source tree.
	 * @param visualContainer The current element of the visual tree.
	 */
	public void setPseudoContent(VpeTemplateContext context, Node sourceContainer, Node visualContainer,
			Document visualDocument) {
		try{
		if (pseudoContentCreator != null) {
			pseudoContentCreator.setPseudoContent(context, sourceContainer,
					visualContainer, visualDocument);
		} else {
			VpeDefaultPseudoContentCreator.getInstance().setPseudoContent(context, sourceContainer, visualContainer,
					visualDocument);
		}
		} catch (VpeExpressionException ex) {
			
			VpeExpressionException exception = new VpeExpressionException(sourceContainer+" ",ex); //$NON-NLS-1$
			VpePlugin.reportProblem(exception);
		}
	}

	/**
	 * Contains text.
	 * 
	 * @return true, if contains text
	 */
	public boolean containsText() {
		return true;
	}

	/**
	 * Checks for imaginary border.
	 * 
	 * @return true, if has imaginary border
	 */
	public boolean hasImaginaryBorder() {
		return hasImaginaryBorder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.vpe.editor.template.VpeTemplate#openBundle(org.jboss.
	 * tools.vpe.editor.context.VpePageContext,
	 * org.mozilla.interfaces.nsIDOMNode,
	 * org.jboss.tools.vpe.editor.mapping.VpeElementMapping)
	 */
	
	public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	final public double getPriority() {
		return priority;
	}
	
	final public void setPriority(double priority) {
		this.priority = priority;
	}
	
}
