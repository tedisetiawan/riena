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
package org.eclipse.riena.communication.core.zipsupport;

class BufferEntry {

	int BUFFERSIZE = 8192;
	byte[] buffer;

	BufferEntry() {
		super();
		buffer = new byte[BUFFERSIZE];
	}
}