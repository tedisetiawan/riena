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
package org.eclipse.riena.internal.navigation.ui.filter;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.ui.filter.AbstractUIFilterMarkerAttribute;

/**
 *
 */
public abstract class AbstractNavigationUIFilterMarkerAttribute extends AbstractUIFilterMarkerAttribute {

	private INavigationNode<?> node;

	public AbstractNavigationUIFilterMarkerAttribute(INavigationNode<?> node, IMarker marker) {
		super(marker);
		this.node = node;
	}

	public boolean matches(Object object) {

		if (object == node) {
			return true;
		}
		if (object instanceof INavigationNode<?>) {
			INavigationNode<?> pNode = (INavigationNode<?>) object;
			return matches(pNode);
		}

		return false;

	}

	private boolean matches(INavigationNode<?> pNode) {

		if (pNode == node) {
			return true;
		} else if (pNode.getParent() != null) {
			return matches(pNode.getParent());
		}

		return false;

	}

	public void apply(Object object) {
		node.addMarker(getMarker());
	}

	public void remove(Object object) {
		node.removeMarker(getMarker());
	}

}
