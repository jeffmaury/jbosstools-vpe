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
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Used for processing templates with renderd="false" attribute.
 * 
 * @author mareshkau
 *
 */
public class VpeRenderingTemplate extends VpeAbstractTemplate{
	
	private static VpeRenderingTemplate instance;
	
	private VpeRenderingTemplate(){
		
	}
	
	public static synchronized  VpeRenderingTemplate getInstance(){
		if(instance==null)  {
			instance = new VpeRenderingTemplate();
		}
		return instance;
	}
	
	@Override
	public VpeCreationData create(VpeTemplateContext contexnt, Node sourceNode,
			Document visualDocument) {
		return  new VpeCreationData(null);
	}

}
