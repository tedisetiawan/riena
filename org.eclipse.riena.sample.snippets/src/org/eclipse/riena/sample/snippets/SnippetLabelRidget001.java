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
package org.eclipse.riena.sample.snippets;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Text field ridget with minimum length validation rule and direct writing.
 */
public final class SnippetLabelRidget001 {

	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			Shell shell = new Shell();
			GridLayoutFactory.fillDefaults().numColumns(2).margins(10, 10).spacing(20, 10).applyTo(shell);

			createLabel(shell, "DateTime:"); //$NON-NLS-1$
			Label dateTimeWidget = createLabel(shell, ""); //$NON-NLS-1$
			GridDataFactory.fillDefaults().grab(true, false).applyTo(dateTimeWidget);

			final DateBean dateBean = new DateBean();

			final ILabelRidget dateTimeRidget = (ILabelRidget) SwtRidgetFactory.createRidget(dateTimeWidget);
			dateTimeRidget.bindToModel(BeansObservables.observeValue(dateBean, "time")); //$NON-NLS-1$

			final Timer t = new Timer();
			t.schedule(new TimerTask() {

				@Override
				public void run() {
					Display.getDefault().asyncExec(new Runnable() {

						public void run() {
							try {
								dateBean.update(); // update bean to the current value for date & time
								dateTimeRidget.updateFromModel(); // update Ridget from bean
							} catch (SWTException e) {
								t.cancel();
							}
						}

					});
				}

			}, new Date(), 1000);

			shell.setSize(200, 200);
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} finally {
			display.dispose();
		}
	}

	private static Label createLabel(Shell shell, String caption) {
		Label result = new Label(shell, SWT.NONE);
		result.setText(caption);
		return result;
	}

	private static class DateBean {

		private Date time;

		DateBean() {
			update();
		}

		public void update() {
			time = Calendar.getInstance().getTime();
		}

		public Date getTime() {
			return time;
		}
	}
}
