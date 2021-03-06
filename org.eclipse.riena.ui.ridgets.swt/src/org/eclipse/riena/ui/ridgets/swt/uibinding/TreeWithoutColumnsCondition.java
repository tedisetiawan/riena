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
package org.eclipse.riena.ui.ridgets.swt.uibinding;

import org.eclipse.swt.widgets.Tree;

import org.eclipse.riena.ui.ridgets.uibinding.IMappingCondition;

/**
 * This mapping condition returns true if the given widget is a {@link Tree}
 * with exactly zero columns.
 */
public final class TreeWithoutColumnsCondition implements IMappingCondition {

	public boolean isMatch(final Object widget) {
		boolean result = false;
		if (widget instanceof Tree) {
			result = ((Tree) widget).getColumnCount() == 0;
		}
		return result;
	}

}
