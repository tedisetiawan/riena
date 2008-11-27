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
package org.eclipse.riena.ui.ridgets.validation;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.riena.core.util.PropertiesUtils;

/**
 * 
 */
public class MaxLength implements IValidator, IExecutableExtension {

	protected int maxLength;

	public MaxLength() {
	}

	public MaxLength(int length) {
		this.maxLength = length;
	}

	/**
	 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
	 */
	public IStatus validate(Object value) {

		if (value == null || maxLength < 0) {
			return ValidationRuleStatus.ok();
		}

		if (value instanceof String) {
			String string = (String) value;
			int length = string.length();

			if (length > maxLength) {
				return ValidationRuleStatus.error(true, "'" + string + "' must not be longer than " + maxLength //$NON-NLS-1$ //$NON-NLS-2$
						+ " characters.", this); //$NON-NLS-1$
			}
			return ValidationRuleStatus.ok();
		} else {
			throw new ValidationFailure("MaxLength can only validate objects of type String."); //$NON-NLS-1$
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder(this.getClass().getSimpleName());
		buffer.append("[maxLength="); //$NON-NLS-1$
		buffer.append(maxLength);
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}

	/**
	 * This method is called on a newly constructed extension for validation.
	 * After creating a new instance of {@code MaxLength} this method is called
	 * to initialize the instance. The argument for initialization is in the
	 * parameter {@code data}. Is the data a string the argument is the initial
	 * value of {@code maxLength}.
	 * 
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement,
	 *      java.lang.String, java.lang.Object)
	 */
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {

		if (data instanceof String) {
			String[] args = PropertiesUtils.asArray((String) data);
			maxLength = Integer.parseInt(args[0]);
		}

	}

}
