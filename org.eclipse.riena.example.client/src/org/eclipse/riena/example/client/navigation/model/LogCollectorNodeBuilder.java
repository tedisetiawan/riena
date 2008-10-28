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

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeBuilder;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;

/**
 *
 */
public class LogCollectorNodeBuilder implements INavigationNodeBuilder {

	public INavigationNode<?> buildNode(NavigationNodeId navigationNodeId, NavigationArgument navigationArgument) {

		IModuleGroupNode moduleGroup = new ModuleGroupNode(navigationNodeId);

		IModuleNode filtersModule = new ModuleNode(null, "LogCollector"); //$NON-NLS-1$
		moduleGroup.addChild(filtersModule);

		ISubModuleNode filterRidgetSubModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.logcollectorlog"), "Log Collector"); //$NON-NLS-1$ //$NON-NLS-2$
		filtersModule.addChild(filterRidgetSubModule);

		return moduleGroup;
	}

}
