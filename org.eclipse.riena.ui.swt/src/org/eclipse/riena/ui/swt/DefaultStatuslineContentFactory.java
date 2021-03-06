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
package org.eclipse.riena.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.core.util.RAPDetector;
import org.eclipse.riena.core.util.ReflectionUtils;

/**
 * Default implementation of {@link IStatusLineContentFactory}. Layouts the
 * existing {@link StatuslineNumber} and the {@link StatuslineUIProcess} held by
 * the {@link Statusline}. Adds additional
 * 
 * @since 1.2
 */
public class DefaultStatuslineContentFactory implements IStatusLineContentFactory {

	/**
	 * Default implementation. Consider that {@link StatuslineNumber} and
	 * {@link StatuslineUIProcess} are allready added by using addUIControl
	 * inside the {@link Statusline}. These just need to be layouted.
	 */
	public void createContent(final Statusline statusline) {
		statusline.setLayout(new FormLayout());

		FormData formData = null;
		Control lastControl = null;
		Control spacerControl = null;

		if (!RAPDetector.isRAPavailable()) {
			final StatuslineTime time = new StatuslineTime(statusline, SWT.NONE);
			formData = new FormData();
			formData.top = new FormAttachment(0, 0);
			formData.bottom = new FormAttachment(100, 0);
			formData.right = new FormAttachment(100, 0);
			time.setLayoutData(formData);
			lastControl = time;

			spacerControl = createSpacer(statusline);
			if (spacerControl != null) {
				setSpaceLayoutData(lastControl, spacerControl);
				lastControl = spacerControl;
			}

			final StatuslineDate date = new StatuslineDate(statusline, SWT.NONE);
			formData = new FormData();
			formData.top = new FormAttachment(0, 0);
			formData.bottom = new FormAttachment(100, 0);
			formData.right = new FormAttachment(lastControl, 0);
			date.setLayoutData(formData);
			lastControl = date;

			spacerControl = createSpacer(statusline);
			if (spacerControl != null) {
				setSpaceLayoutData(lastControl, spacerControl);
				lastControl = spacerControl;
			}
		}

		final StatuslineNumber number = createStatuslineNumber(statusline);
		statusline.addUIControl(number, Statusline.SL_NUMBER_RIDGET_ID);
		FormData formData1;
		formData1 = new FormData();
		formData1.top = new FormAttachment(0, 0);
		formData1.bottom = new FormAttachment(100, 0);
		if (RAPDetector.isRAPavailable()) {
			formData1.right = new FormAttachment(100, 0);
		} else {
			formData1.right = new FormAttachment(lastControl, 0);
		}
		number.setLayoutData(formData1);

		lastControl = number;

		spacerControl = createSpacer(statusline);
		if (spacerControl != null) {
			setSpaceLayoutData(lastControl, spacerControl);
			lastControl = spacerControl;
		}

		final StatuslineUIProcess uiProcess = new StatuslineUIProcess(statusline, SWT.NONE);
		statusline.addUIControl(uiProcess, Statusline.SL_UIPROCES_RIDGET_ID);

		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.right = new FormAttachment(lastControl, 0);
		uiProcess.setLayoutData(formData);
		lastControl = uiProcess;

		spacerControl = createSpacer(statusline);
		if (spacerControl != null) {
			setSpaceLayoutData(lastControl, spacerControl);
			lastControl = spacerControl;
		}

		final StatuslineMessage message = statusline.getMessageComposite();
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(lastControl, 0);
		message.setLayoutData(formData);
		lastControl = message;

	}

	private void setSpaceLayoutData(Control lastControl, Control spacerControl) {
		FormData formData;
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.right = new FormAttachment(lastControl, 0);
		spacerControl.setLayoutData(formData);
	}

	protected StatuslineNumber createStatuslineNumber(final Statusline statusline) {
		return new StatuslineNumber(statusline, SWT.NONE);
	}

	//
	//	protected Control layoutStatuslineNumber(Control lastControl, StatuslineNumber number) {
	//		FormData formData;
	//		formData = new FormData();
	//		formData.top = new FormAttachment(0, 0);
	//		formData.bottom = new FormAttachment(100, 0);
	//		formData.right = new FormAttachment(lastControl, 0);
	//		number.setLayoutData(formData);
	//		lastControl = number;
	//		return lastControl;
	//	}

	/**
	 * Creates a spacer.
	 */
	protected Control createSpacer(final Statusline statusline) {

		Control result = null;
		final Class<? extends Control> spacer = statusline.getSpacer();
		if (spacer != null) {
			try {
				result = ReflectionUtils.newInstance(spacer, statusline, SWT.NONE);
			} catch (final Exception e) {
				result = null;
			}
		}

		return result;

	}

}
