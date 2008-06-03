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
package org.jboss.tools.vpe.editor.template.expression;

import org.jboss.tools.vpe.messages.VpeUIMessages;

public abstract class VpeFunction extends VpeOperand {
	private static final String ERROR_PARAMETER_INCORRECT = VpeUIMessages.INCORRECT_PARAMETER_ERROR;
	
	private VpeOperand[] paramertes;
	
	VpeFunction() {
	}
	
	void setParameters(VpeOperand[] paramertes) {
		this.paramertes = paramertes;
	}
	
	int getPriority() {
		return PRIORITY_OPERAND;
	}

	VpeOperand getParameter(int index) {
		if (paramertes == null || paramertes.length < index) {
			throw new VpeExpressionError(ERROR_PARAMETER_INCORRECT);
		}
		return paramertes[index];
	}
	
	String[] getSignatures() {
		return null;
	}
}
