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
package org.eclipse.riena.ui.filter.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.filter.IUIFilterContainer;
import org.eclipse.riena.ui.filter.IUIFilterProvider;
import org.eclipse.riena.ui.filter.IUIFilterRule;
import org.eclipse.riena.ui.filter.IUIFilterRuleMarkerNavigation;
import org.eclipse.riena.ui.filter.IUIFilterRuleMarkerRidget;
import org.eclipse.riena.ui.filter.IUIFilterRuleValidator;
import org.eclipse.riena.ui.filter.IUIFilterRuleValidatorRidget;
import org.eclipse.riena.ui.filter.extension.IRuleMarkerMenuItem;
import org.eclipse.riena.ui.filter.extension.IRuleMarkerNavigation;
import org.eclipse.riena.ui.filter.extension.IRuleMarkerRidget;
import org.eclipse.riena.ui.filter.extension.IRuleValidatorRidget;
import org.eclipse.riena.ui.filter.extension.IUIFilterExtension;
import org.eclipse.riena.ui.internal.Activator;

/**
 * 
 */
public class UIFilterProvider implements IUIFilterProvider {

	private static final String EP_UIFILTER = "org.eclipse.riena.filter.uifilter"; //$NON-NLS-1$
	private IUIFilterExtension[] data;

	/**
	 * Creates a new instance of {@code UIFilterProvider} and perform
	 * configuration.
	 */
	public UIFilterProvider() {
		this(true);
	}

	/**
	 * Constructor that should only be used while testing
	 * 
	 * @param autoConfig
	 *            - true perform configuration; otherwise do not configure
	 */
	public UIFilterProvider(boolean autoConfig) {
		if (autoConfig) {
			Inject.extension(EP_UIFILTER).into(this).andStart(Activator.getDefault().getContext());
		}
	}

	/**
	 * Returns the extension for the given ID.
	 * 
	 * @param filterId
	 *            - filter ID
	 * @return extension or {@code null} if no extension was found
	 */
	protected IUIFilterExtension getUIFilterDefinition(String filterId) {

		if (getData().length == 0 || filterId == null) {
			return null;
		} else {
			for (IUIFilterExtension extension : getData()) {
				if ((extension.getFilterId() != null) && (extension.getFilterId().equals(filterId))) {
					return extension;
				}
			}
		}

		return null;

	}

	public IUIFilterContainer provideFilter(String filterID) {

		IUIFilterExtension filterExtension = getUIFilterDefinition(filterID);

		Collection<IUIFilterRule> rules = new ArrayList<IUIFilterRule>(1);

		RulesProvider rulesProvider = new RulesProvider();

		// rules for marker/ridget
		for (IRuleMarkerRidget ruleExtension : filterExtension.getRuleMarkerRidgets()) {

			String markerType = ruleExtension.getMarker();
			IUIFilterRuleMarkerRidget rule = rulesProvider.getRuleMarkerRidget(markerType);
			if (rule != null) {
				String id = ruleExtension.getRidgetId();
				rule.setId(id);
				rules.add(rule);
			}

		}

		// rules for marker/menu- and toolItems
		for (IRuleMarkerMenuItem ruleExtension : filterExtension.getRuleMarkerMenuItems()) {

			String markerType = ruleExtension.getMarker();
			IUIFilterRuleMarkerRidget rule = rulesProvider.getRuleMarkerMenuItem(markerType);
			if (rule != null) {
				String id = ruleExtension.getItemId();
				rule.setId(id);
				rules.add(rule);
			}

		}

		// rules for marker/navigation
		for (IRuleMarkerNavigation ruleExtension : filterExtension.getRuleMarkerNavigations()) {

			String markerType = ruleExtension.getMarker();
			IUIFilterRuleMarkerNavigation rule = rulesProvider.getRuleMarkerNavigation(markerType);
			if (rule != null) {
				String id = ruleExtension.getNodeId();
				rule.setNode(id);
				rules.add(rule);
			}

		}

		// rules for validator
		for (IRuleValidatorRidget ruleExtension : filterExtension.getRuleValidatorRidgets()) {

			IUIFilterRuleValidator rule = createRuleValidatorRidget(ruleExtension);
			if (rule != null) {
				rules.add(rule);
			}

		}

		UIFilter filterResult = new UIFilter(filterID, rules);

		return new UIFilterContainer(filterResult, filterExtension.getNodeIds());

	}

	/**
	 * Creates a rule to add an validator to a ridget.
	 * 
	 * @param ruleExtension
	 *            - extension that defines the rule
	 * @return rule or {@code null} if no rule was created
	 */
	private IUIFilterRuleValidator createRuleValidatorRidget(IRuleValidatorRidget ruleExtension) {

		RulesProvider rulesProvider = new RulesProvider();
		IUIFilterRuleValidatorRidget rule = rulesProvider.getRuleValidatorRidget();
		if (rule != null) {
			rule.setId(ruleExtension.getRidgetId());
			String timeString = ruleExtension.getValidationTime();
			ValidationTime time = ValidationTime.ON_UPDATE_TO_MODEL;
			if (timeString != null) {
				if (timeString.equals("onUIControlEdit")) { //$NON-NLS-1$
					time = ValidationTime.ON_UI_CONTROL_EDIT;
				}
			}
			rule.setValidationTime(time);
			IValidator validator = ruleExtension.getValidator();
			rule.setValidator(validator);
		}

		return rule;

	}

	/**
	 * This is the callback method for the extension injector.
	 * 
	 * @param data
	 */
	public void update(IUIFilterExtension[] data) {
		this.data = data;
	}

	private IUIFilterExtension[] getData() {
		return data;
	}

}
