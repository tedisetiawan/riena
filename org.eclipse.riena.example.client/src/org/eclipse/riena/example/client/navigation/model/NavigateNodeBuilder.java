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
package org.eclipse.riena.example.client.navigation.model;

import org.eclipse.riena.example.client.application.ExampleIcons;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeId;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubModuleNode;

/**
 *
 */
public class NavigateNodeBuilder extends NavigationNodeBuilder {

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeBuilder#buildNode(org.eclipse.riena.navigation.INavigationNodeId,
	 *      org.eclipse.riena.navigation.NavigationArgument)
	 */
	public INavigationNode<?> buildNode(INavigationNodeId nodeId, NavigationArgument navigationArgument) {
		IModuleGroupNode moduleGroup = new ModuleGroupNode(nodeId);
		moduleGroup.setPresentWithSingleModule(false);
		IModuleNode module = new ModuleNode(null, "Navigate"); //$NON-NLS-1$
		module.setIcon(createIconPath(ExampleIcons.ICON_GREEN_LED));
		moduleGroup.addChild(module);
		ISubModuleNode subModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.navigate.form"), "Navigate"); //$NON-NLS-1$ //$NON-NLS-2$
		module.addChild(subModule);
		return moduleGroup;
	}

}
