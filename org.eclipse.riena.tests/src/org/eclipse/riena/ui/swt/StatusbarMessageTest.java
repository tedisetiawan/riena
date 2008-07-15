/*******************************************************************************
 * Copyright (c) 2007, 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.swt;

import junit.framework.TestCase;

import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests of the class {@link StatusbarMessage}.
 */
public class StatusbarMessageTest extends TestCase {

	private Shell shell;
	private StatusbarMessage statusMessage;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		shell = new Shell();
		statusMessage = new StatusbarMessage(shell, SWT.NONE);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.disposeWidget(shell);
		SwtUtilities.disposeWidget(statusMessage);
	}

	/**
	 * Tests the method {@code createContents()}.<br>
	 * <i>The method is already called by the constructor.</i>
	 */
	public void testCreateContents() {

		Control[] controls = statusMessage.getChildren();
		assertEquals(1, controls.length);
		assertTrue(controls[0] instanceof Label);

	}

	/**
	 * Tests the method {@code setMessage()}.
	 */
	public void testSetMessage() {

		statusMessage.setMessage("Hello!");

		Control[] controls = statusMessage.getChildren();
		Label label = (Label) controls[0];
		assertEquals("Hello!", label.getText());

	}

}
