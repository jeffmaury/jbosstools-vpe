/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.template;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.graphics.Point;
import org.jboss.tools.jst.web.ui.internal.editor.editor.ITextFormatter;
import org.jboss.tools.jst.web.ui.internal.editor.selection.SourceSelection;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeSourceDropInfo;
import org.jboss.tools.vpe.editor.VpeSourceInnerDragInfo;
import org.jboss.tools.vpe.editor.context.AbstractPageContext;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.NodeData;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager.VpeTemplateContext;
import org.jboss.tools.vpe.editor.template.textformating.TextFormatingData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VpeTemplateSafeWrapper implements VpeTemplate, IAdaptable {

	VpeTemplate delegate;

	public VpeTemplateSafeWrapper(VpeTemplate delegate) {
		super();
		this.delegate = delegate;
	}

	/**
	 * @param templateElement
	 * @param caseSensitive
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#init(org.w3c.dom.Element, boolean)
	 */
	public void init(Element templateElement, boolean caseSensitive) {
		try {
			delegate.init(templateElement, caseSensitive);
		} catch(Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		} catch (LinkageError ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
	}

	/**
	 * @param context
	 * @param sourceNode
	 * @param visualDocument
	 * @return
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#create(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMDocument)
	 */
	public VpeCreationData create(VpeTemplateContext context, Node sourceNode,
			Document visualDocument) {
		try {
			return delegate.create(context, sourceNode, visualDocument);
		} catch(Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		} catch (LinkageError ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return  null;
	}



	/**
	 * @return
	 * @deprecated
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#getType()
	 */
	public int getType() {
		try {
			return delegate.getType();
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return VpeHtmlTemplate.TYPE_ANY;
	}

	/**
	 * @return
	 * @deprecated
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#getAnyData()
	 */
	public VpeAnyData getAnyData() {
		try {
			return delegate.getAnyData();
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return null;
	}

    
	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#getPriority()
	 */
	public double getPriority() {
		try {
			return delegate.getPriority();
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return 0.0;
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#setPriority(double)
	 */
	public void setPriority(double priority) {
		try {
			delegate.setPriority(priority);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == VpeToggableTemplate.class) {
			return castDelegateTo(VpeToggableTemplate.class);
		} 
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}
	
	public Object castDelegateTo(Class adapter) {
		Object result = null;
		try {
			if (adapter.isInstance(delegate)) {
				result = adapter.cast(delegate);
			}
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return result;
	}

	@Override
	public String toString() {
		return "VpeTemplateSafeWrapper@" + this.hashCode()  //$NON-NLS-1$
				+ " [delegate=" + delegate + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}
	
}
