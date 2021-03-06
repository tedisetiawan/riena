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
package org.eclipse.riena.example.client.controllers;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.observable.list.WritableList;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.example.client.views.MasterDetailsSubModuleView;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.AbstractMasterDetailsDelegate;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;
import org.eclipse.riena.ui.ridgets.IMultipleChoiceRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.annotation.OnFocusLost;
import org.eclipse.riena.ui.ridgets.validation.NotEmpty;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;

/**
 * Demonstrates use of a master/details ridget.
 * 
 * @see IMasterDetailsRidget
 * @see MasterDetailsSubModuleView
 */
public class MasterDetailsSubModuleController extends SubModuleController {

	/**
	 * Setup the ridgets for editing a person (text ridgets for name, single
	 * choice ridget for gender, multiple choice ridgets for pets).
	 */
	public static final class PersonDelegate extends AbstractMasterDetailsDelegate {

		private static final String[] GENDER = { Person.FEMALE, Person.MALE };

		private final Person workingCopy = createWorkingCopy();

		public void configureRidgets(final IRidgetContainer container) {
			final ITextRidget txtFirst = container.getRidget(ITextRidget.class, "first"); //$NON-NLS-1$
			txtFirst.setMandatory(true);
			txtFirst.bindToModel(workingCopy, Person.PROPERTY_FIRSTNAME);
			txtFirst.updateFromModel();

			final ITextRidget txtLast = container.getRidget(ITextRidget.class, "last"); //$NON-NLS-1$
			txtLast.setMandatory(true);
			txtLast.addValidationRule(new NotEmpty(), ValidationTime.ON_UI_CONTROL_EDIT);
			txtLast.bindToModel(workingCopy, Person.PROPERTY_LASTNAME);
			txtLast.updateFromModel();

			final ISingleChoiceRidget gender = container.getRidget(ISingleChoiceRidget.class, "gender"); //$NON-NLS-1$
			if (gender != null) {
				gender.bindToModel(Arrays.asList(GENDER), (List<String>) null, workingCopy, Person.PROPERTY_GENDER);
				gender.updateFromModel();
			}

			final IMultipleChoiceRidget pets = container.getRidget(IMultipleChoiceRidget.class, "pets"); //$NON-NLS-1$
			if (pets != null) {
				pets.bindToModel(Arrays.asList(Person.Pets.values()), (List<String>) null, workingCopy,
						Person.PROPERTY_PETS);
				pets.updateFromModel();
			}
		}

		@OnFocusLost(ridgetId = "first")
		public void dumpFocusLost() {
			System.out.println("Text field \"first\" has not longer the focus!");
		}

		public Person createWorkingCopy() {
			return new Person("", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}

		public Person copyBean(final Object source, final Object target) {
			final Person from = (Person) source;
			final Person to = (Person) target;
			to.setFirstname(from.getFirstname());
			to.setLastname(from.getLastname());
			to.setGender(from.getGender());
			to.setPets(from.getPets());
			return to;
		}

		public Object getWorkingCopy() {
			return workingCopy;
		}

		@Override
		public boolean isChanged(final Object source, final Object target) {
			final Person p1 = (Person) source;
			final Person p2 = (Person) target;
			final boolean equals = p1.getFirstname().equals(p2.getFirstname())
					&& p1.getLastname().equals(p2.getLastname()) && p1.getGender().equals(p2.getGender())
					&& p1.getPets().equals(p2.getPets());
			return !equals;
		}

		@Override
		public String isValid(final IRidgetContainer container) {
			final ITextRidget txtLast = container.getRidget(ITextRidget.class, "last"); //$NON-NLS-1$
			if (txtLast.isErrorMarked()) {
				return "'Last Name' is not valid."; //$NON-NLS-1$
			}
			return null;
		}
	}

	private final List<Person> input = PersonFactory.createPersonList();

	@Override
	public void configureRidgets() {
		final String[] properties = new String[] { "firstname", "lastname" }; //$NON-NLS-1$ //$NON-NLS-2$
		final String[] headers = new String[] { "First Name", "Last Name" }; //$NON-NLS-1$ //$NON-NLS-2$

		final IMasterDetailsRidget master = getRidget(IMasterDetailsRidget.class, "master"); //$NON-NLS-1$
		master.setDelegate(new PersonDelegate());
		master.bindToModel(new WritableList(input, Person.class), Person.class, properties, headers);
		master.updateFromModel();

		final IActionRidget actionApply = master.getRidget(IActionRidget.class, MasterDetailsComposite.BIND_ID_APPLY);
		addDefaultAction(master, actionApply);

		final IActionRidget enableDisableButton = getRidget(IActionRidget.class, "enableDisable"); //$NON-NLS-1$
		enableDisableButton.addListener(new IActionListener() {
			public void callback() {
				master.setEnabled(!master.isEnabled());
			}
		});
	}

}
