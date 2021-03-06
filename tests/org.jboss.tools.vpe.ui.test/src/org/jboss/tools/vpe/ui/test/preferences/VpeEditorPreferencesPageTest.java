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
package org.jboss.tools.vpe.ui.test.preferences;

import org.jboss.tools.tests.PreferencePageTest;
import org.jboss.tools.vpe.editor.preferences.ELVariablesPreferencePage;
import org.jboss.tools.vpe.editor.preferences.VpeEditorPreferencesPage;
import org.junit.Test;

public class VpeEditorPreferencesPageTest extends PreferencePageTest {
	
	public VpeEditorPreferencesPageTest() {
	}
	@Test
	public void testVpeEditorPreferencesPageShow() {
		doDefaultTest(VpeEditorPreferencesPage.ID, VpeEditorPreferencesPage.class); //$NON-NLS-1$
	}
	@Test
	public void testVpeEditorELPreferencesPageShow() {
		doDefaultTest(ELVariablesPreferencePage.ID, ELVariablesPreferencePage.class); //$NON-NLS-1$
	}
}
