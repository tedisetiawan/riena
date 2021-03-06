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
package org.eclipse.riena.core.injector.extension;

/**
 *
 */
@ExtensionInterface
public interface IWireData {

	@MapName("objectType")
	IWireable createObjectTypeWithWire();

	@MapName("objectType")
	@CreateLazy
	IWireable createLazyObjectTypeWithWire();

	@DoNotWireExecutable
	@MapName("objectType")
	IWireable createObjectTypeWithoutWire();

	@DoNotWireExecutable
	@MapName("objectType")
	@CreateLazy
	IWireable createLazyObjectTypeWithoutWire();
}
