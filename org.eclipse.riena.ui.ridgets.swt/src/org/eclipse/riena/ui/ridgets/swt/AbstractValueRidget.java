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

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;

import org.eclipse.riena.ui.ridgets.IValueRidget;
import org.eclipse.riena.ui.ridgets.ValueBindingSupport;

/**
 * Abstract implementation of an {@link IValueRidget} for SWT.
 */
public abstract class AbstractValueRidget extends AbstractSWTRidget implements IValueRidget {

	private final ValueBindingSupport valueBindingSupport;

	public AbstractValueRidget() {
		valueBindingSupport = new ValueBindingSupport(this.getRidgetObservable());
		valueBindingSupport.setMarkable(this);
	}

	/**
	 * @return The observable value of the Ridget.
	 */
	protected abstract IObservableValue getRidgetObservable();

	public void bindToModel(final IObservableValue observableValue) {
		valueBindingSupport.bindToModel(observableValue);
	}

	public void bindToModel(final Object valueHolder, final String valuePropertyName) {
		valueBindingSupport.bindToModel(valueHolder, valuePropertyName);
	}

	public IConverter getModelToUIControlConverter() {
		return valueBindingSupport.getModelToUIControlConverter();
	}

	public void setModelToUIControlConverter(final IConverter converter) {
		valueBindingSupport.setModelToUIControlConverter(converter);
	}

	@Override
	public void updateFromModel() {
		super.updateFromModel();
		valueBindingSupport.updateFromModel();
	}

	/**
	 * @since 4.0
	 */
	public final ValueBindingSupport getValueBindingSupport() {
		return valueBindingSupport;
	}

	/**
	 * @since 4.0
	 */
	public String getValuePropertyName() {
		return valueBindingSupport.getValuePropertyName();
	}
}
