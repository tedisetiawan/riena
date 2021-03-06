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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ProgressBar;

import org.eclipse.riena.ui.ridgets.IProgressBarRidget;

/**
 * Ridget for a SWT {@link ProgressBar} widget.
 */
public class ProgressBarRidget extends AbstractTraverseRidget implements IProgressBarRidget {

	private boolean initialized;
	private boolean maxInitialized;
	private boolean minInitialized;

	@Override
	public ProgressBar getUIControl() {
		return (ProgressBar) super.getUIControl();
	}

	@Override
	public void checkUIControl(final Object uiControl) {
		checkType(uiControl, ProgressBar.class);
	}

	@Override
	protected int getValue(final Control control) {
		return getUIControl() != null ? getUIControl().getSelection() : 0;
	}

	@Override
	protected void initFromUIControl() {
		final ProgressBar bar = getUIControl();
		if (bar != null && !initialized) {
			if (!maxInitialized) {
				setMaximum(bar.getMaximum());
			}
			if (!minInitialized) {
				setMinimum(bar.getMinimum());
			}
			setValue(bar.getSelection());
			initialized = true;
		}
	}

	@Override
	protected void updateUIMaximum() {
		final ProgressBar control = getUIControl();
		if (control != null) {
			control.setMaximum(getMaximum());
		}

	}

	@Override
	protected void updateUIMinimum() {
		final ProgressBar control = getUIControl();
		if (control != null) {
			control.setMinimum(getMinimum());
		}

	}

	@Override
	protected void updateUIValue() {
		final ProgressBar control = getUIControl();
		if (control != null) {
			control.setSelection(getValue());
		}

	}

	@Override
	protected void addSelectionListener(final Control control, final SelectionListener listener) {
		// not needed here
	}

	@Override
	protected void removeSelectionListener(final Control control, final SelectionListener listener) {
		// not needed here
	}

	@Override
	protected int getUIControlIncrement() {
		// not needed here
		return 0;
	}

	@Override
	protected int getUIControlMaximum() {
		// not needed here
		return 0;
	}

	@Override
	protected int getUIControlMinimum() {
		// not needed here
		return 0;
	}

	@Override
	protected int getUIControlPageIncrement() {
		// not needed here
		return 0;
	}

	@Override
	protected int getUIControlSelection() {
		// not needed here
		return 0;
	}

	@Override
	protected void initAdditionalsFromUIControl() {
		// not needed here
	}

	@Override
	protected void updateUIIncrement() {
		// not needed here
	}

	@Override
	protected void updateUIPageIncrement() {
		// not needed here
	}

	@Override
	public void setMaximum(final int maximum) {
		super.setMaximum(maximum);
		maxInitialized = true;
	}

	@Override
	public void setMinimum(final int minimum) {
		super.setMinimum(minimum);
		minInitialized = true;
	}

}
