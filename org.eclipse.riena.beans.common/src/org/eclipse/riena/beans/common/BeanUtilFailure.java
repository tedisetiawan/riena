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
package org.eclipse.riena.beans.common;

import org.eclipse.riena.core.util.UtilFailure;

/**
 * Failure class for bean util specific failures.
 */
public class BeanUtilFailure extends UtilFailure {

	private static final long serialVersionUID = -6871383716536677582L;

	/**
	 * Failure with a simple message.
	 * 
	 * @param msg
	 *            the message
	 */
	public BeanUtilFailure(final String msg) {
		super(msg);
	}

	/**
	 * Failure with a simple message and a cause.
	 * 
	 * @param msg
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public BeanUtilFailure(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
