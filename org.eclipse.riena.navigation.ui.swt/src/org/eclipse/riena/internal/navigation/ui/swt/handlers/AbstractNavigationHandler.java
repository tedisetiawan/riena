/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.navigation.ui.swt.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;

import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;

/**
 * Abstract handler for navigation related actions.
 * <p>
 * Extend and implement
 * {@link #execute(org.eclipse.core.commands.ExecutionEvent)} to use.
 */
abstract class AbstractNavigationHandler extends AbstractHandler {

	/**
	 * TODO [ev] docs
	 * 
	 * @param application
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected final IModuleNode[] collectModules(IApplicationNode application) {
		List<IModuleNode> modules = new ArrayList<IModuleNode>();
		INavigationNode<?> subApplication = findActive((List) application.getChildren());
		if (subApplication instanceof ISubApplicationNode) {
			List<IModuleGroupNode> groups = ((ISubApplicationNode) subApplication).getChildren();
			for (IModuleGroupNode moduleGroup : groups) {
				modules.addAll(moduleGroup.getChildren());
			}
		}
		return modules.toArray(new IModuleNode[modules.size()]);
	}

	/**
	 * Return the first 'active' nodes from the list of nodes.
	 * 
	 * @param nodes
	 *            a list of INavigationNode elements; never null
	 * 
	 * @see INavigationNode#isActivated()
	 */
	protected final INavigationNode<?> findActive(List<INavigationNode<?>> nodes) {
		INavigationNode<?> result = null;
		Iterator<INavigationNode<?>> iter = nodes.iterator();
		while (result == null && iter.hasNext()) {
			INavigationNode<?> candidate = iter.next();
			if (candidate.isActivated()) {
				result = candidate;
			}
		}
		return result;
	}

	/**
	 * Not API; public for testing only.
	 * <p>
	 * Do a depth-first-search of the navigation tree, starting with the
	 * IApplicationNode and return the first 'active' IModuleGroupNode; or null
	 * if none found.
	 */
	@SuppressWarnings("unchecked")
	public final IModuleGroupNode findModuleGroup(IApplicationNode application) {
		IModuleGroupNode result = null;
		INavigationNode<?> subApplication = findActive((List) application.getChildren());
		if (subApplication != null) {
			INavigationNode<?> moduleGroup = findActive((List) subApplication.getChildren());
			if (moduleGroup instanceof IModuleGroupNode) {
				result = (IModuleGroupNode) moduleGroup;
			}
		}

		return result;
	}

	/**
	 * Not API; public for testing only.
	 * <p>
	 * Find the currently 'selected' node and return it's successor. If the
	 * 'selected' node is the last node of the array, return the first node. If
	 * no successor can be determined (i.e.e only one node, several selected
	 * nodes) return null.
	 * <p>
	 * The notion of 'selected' depends on the implementation of the
	 * {@link #isSelected(INavigationNode)} method.
	 */
	public final INavigationNode<?> findNextNode(INavigationNode<?>[] nodes) {
		INavigationNode<?> result = null;
		int selectedCount = 0;
		for (INavigationNode<?> node : nodes) {
			if (isSelected(node)) {
				selectedCount++;
			} else {
				if (selectedCount == 1 && result == null) { // previous else implies !selected
					result = node;
				}
			}
		}
		if (selectedCount == 1 && result == null && !isSelected(nodes[0])) { // wrap around
			result = nodes[0];
		}
		return selectedCount == 1 ? result : null;
	}

	/**
	 * Not API; public for testing only.
	 * <p>
	 * Find the currently 'selected' node and return it's predecessor. When
	 * wrapping is on and the 'selected' node is the first node of the array,
	 * return the last node. If no predecessir can be determined (i.e. only one
	 * node, several selected nodes) return null.
	 * <p>
	 * The notion of 'selected' depends on the implementation of the
	 * {@link #isSelected(INavigationNode)} method.
	 */
	public final INavigationNode<?> findPreviousNode(INavigationNode<?>[] nodes, boolean wrap) {
		INavigationNode<?> result = null;
		int selectedCount = 0;
		for (int i = nodes.length - 1; i >= 0; i--) {
			INavigationNode<?> node = nodes[i];
			if (isSelected(node)) {
				selectedCount++;
			} else {
				if (selectedCount == 1 && result == null) { // previous else implies !selected
					result = node;
				}
			}
		}
		int lastApp = nodes.length - 1;
		if (wrap && selectedCount == 1 && result == null && !isSelected(nodes[lastApp])) {
			result = nodes[lastApp];
		}
		return selectedCount == 1 ? result : null;
	}

	// helping methods
	//////////////////

	/**
	 * How to determine if the given <tt>node</tt> is selected. Used by
	 * {@link #findNextNode(INavigationNode[])} and
	 * {@link #findPreviousNode(INavigationNode[])}.
	 * <p>
	 * This implementation will check if node is selected AND it's parent is
	 * selected.
	 * <p>
	 * Subclasses may override.
	 */
	protected boolean isSelected(INavigationNode<?> node) {
		return node.isSelected() && node.getParent().isSelected();
	}

}
