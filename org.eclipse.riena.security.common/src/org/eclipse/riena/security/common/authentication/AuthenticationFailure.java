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
package org.eclipse.riena.security.common.authentication;

import org.eclipse.riena.security.common.SecurityFailure;

/**
 * Failure caused by any fatal problem of the authentication component.
 * 
 * @see org.eclipse.riena.core.exception.Failure
 */
public class AuthenticationFailure extends SecurityFailure {

	private static final long serialVersionUID = 6621574350342798828L;

	/**
	 * Creates a new instance of <code>AuthenticationFailure</code>
	 * 
	 * @param message
	 *            The reason for this exception
	 */
	public AuthenticationFailure(final String message) {
		super(message);
	}

	/**
	 * Creates a new instance of <code>AuthenticationFailure</code>
	 * 
	 * @param message
	 *            The reason for this failure
	 * @param e
	 *            The failure/exception which caused this failure
	 */
	public AuthenticationFailure(final String message, final Throwable e) {
		super(message, e);
	}

}
