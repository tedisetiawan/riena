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
package org.eclipse.riena.ui.ridgets.databinding;

import org.eclipse.core.databinding.conversion.Converter;

/**
 * Converts the input String into an String of lowercase characters.
 * <p>
 * Example: {@code "BIG_Time123!"} -&gt; {@code "big_time123!"}
 * 
 * @since 2.0
 */
public class StringToLowerCaseConverter extends Converter {

	public StringToLowerCaseConverter() {
		super(String.class, String.class);
	}

	public Object convert(final Object fromObject) {
		String result = null;
		if (fromObject != null) {
			final String input = (String) fromObject;
			final StringBuilder builder = new StringBuilder();
			for (int i = 0; i < input.length(); i++) {
				final char ch = input.charAt(i);
				builder.append(Character.toLowerCase(ch));
			}
			result = builder.toString();
		}
		return result;
	}

}
