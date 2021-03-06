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
package org.eclipse.riena.ui.ridgets.swt;

import java.util.Comparator;

import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.ui.common.ISortableByColumn;

/**
 * Changes the result of the given <tt>comparator</tt> according to the
 * <tt>sortedAscending</tt> setting in the ridget.
 */
public final class SortableComparator<T> implements Comparator<T> {

	private final ISortableByColumn ridget;
	private final Comparator<T> orgComparator;

	public SortableComparator(final ISortableByColumn ridget, final Comparator<T> comparator) {
		Assert.isNotNull(ridget);
		Assert.isNotNull(comparator);
		this.ridget = ridget;
		orgComparator = comparator;
	}

	public int compare(final T o1, final T o2) {
		final int result = orgComparator.compare(o1, o2);
		return ridget.isSortedAscending() ? result : result * -1;
	}

	public int getSortedColumn() {
		return ridget.getSortedColumn();
	}
}
