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
package org.eclipse.riena.example.client.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;

public class SalaryIgnoreView extends SubModuleView {

	/**
	 * {@inheritDoc}
	 * <p>
	 * The creation of the content is moved to the {@code SalaryIgnoreComposite}.
	 */
	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setLayout(new RowLayout(SWT.HORIZONTAL));
		new SalaryIgnoreComposite(parent, SWT.None);
	}
}
