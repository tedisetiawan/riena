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
package org.eclipse.riena.communication.core.hooks;

import javax.security.auth.Subject;

/**
 * A concrete {@code AbstractHooksProxy} for testing purposes.
 */
public class HooksProxy extends AbstractHooksProxy {

	private Subject subject;

	/**
	 * @param proxiedInstance
	 */
	public HooksProxy(final Object proxiedInstance) {
		super(proxiedInstance);
	}

	/**
	 * @param proxiedInstance
	 * @param subject
	 */
	public HooksProxy(final Object proxiedInstance, final Subject subject) {
		super(proxiedInstance);
		this.subject = subject;
	}

	@Override
	public Subject getSubject() {
		return subject;
	}

}
