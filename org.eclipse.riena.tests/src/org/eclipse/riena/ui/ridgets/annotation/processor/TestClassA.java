/*******************************************************************************
 * Copyright (c) 2007, 2013 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.annotation.processor;

import org.eclipse.riena.ui.ridgets.annotation.OnActionCallback;

/**
 *
 */
public class TestClassA {

	protected void m1() {
	}

	private void m2() {
	}

	@OnActionCallback(ridgetId = "A.m3")
	protected void m3() {
	}

	@OnActionCallback(ridgetId = "A.m4")
	protected void m4() {
	}

	@OnActionCallback(ridgetId = "A.m5")
	protected void m5() {
	}

}
