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
package org.eclipse.riena.demo.client.rcp.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import org.eclipse.riena.navigation.ApplicationNodeManager;

/**
 * 
 */
public class HistoryBackwardHandler extends DummyHandler {

	/**
	 * @see org.eclipse.riena.demo.client.rcp.handler.DummyHandler#getTitle()
	 */
	@Override
	protected String getTitle() {
		return "History Forward"; //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.riena.demo.client.rcp.handler.DummyHandler#getMessage()
	 */
	@Override
	protected String getMessage() {
		return "History Forward\n"; //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.riena.demo.client.rcp.handler.DummyHandler#execute(org.eclipse
	 *      .core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		ApplicationNodeManager.getApplicationNode().historyBack();
		return null;
	}
}
