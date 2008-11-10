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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtControlRidgetMapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * Tests of the class {@link MenuItemRidget}.
 */
public class MenuItemRidgetTest extends AbstractSWTRidgetTest {

	private final static String PLUGIN_ID = "org.eclipse.riena.ui.tests:";
	private final static String ICON_ECLIPSE = PLUGIN_ID + "/icons/eclipse.gif";

	private final static String LABEL = "testlabel";
	private final static String LABEL2 = "testlabel2";

	@Override
	protected MenuItemRidget createRidget() {
		return new MenuItemRidget();
	}

	@Override
	protected MenuItem createWidget(Composite parent) {
		Menu menu = new Menu(parent);
		return new MenuItem(menu, SWT.NONE);
	}

	@Override
	protected MenuItem getWidget() {
		return (MenuItem) super.getWidget();
	}

	@Override
	protected MenuItemRidget getRidget() {
		return (MenuItemRidget) super.getRidget();
	}

	/**
	 * Tests the constructor {@code MenuItemRidget()}.
	 */
	public void testMenuItemRidget() {

		MenuItemRidget item = new MenuItemRidget();
		boolean textAlreadyInitialized = ReflectionUtils.getHidden(item, "textAlreadyInitialized");
		assertFalse(textAlreadyInitialized);
		boolean useRidgetIcon = ReflectionUtils.getHidden(item, "useRidgetIcon");
		assertFalse(useRidgetIcon);

	}

	public void testRidgetMapping() {
		DefaultSwtControlRidgetMapper mapper = new DefaultSwtControlRidgetMapper();
		assertSame(MenuItemRidget.class, mapper.getRidgetClass(getWidget()));
	}

	public final void testSetText() throws Exception {
		MenuItemRidget ridget = getRidget();
		MenuItem widget = getWidget();

		ridget.setText("");

		assertEquals("", ridget.getText());
		assertEquals("", widget.getText());

		try {
			ridget.setText(null);
			fail();
		} catch (IllegalArgumentException iae) {
			// expected
		}

		ridget.setText(LABEL);

		assertEquals(LABEL, ridget.getText());
		assertEquals(LABEL, widget.getText());

		ridget.setUIControl(null);
		ridget.setText(LABEL2);

		assertEquals(LABEL2, ridget.getText());
		assertEquals(LABEL, widget.getText());

		ridget.setUIControl(widget);

		assertEquals(LABEL2, ridget.getText());
		assertEquals(LABEL2, widget.getText());
	}

	/**
	 * Test method get/setIcon().
	 */
	public final void testSetIcon() {

		MenuItemRidget ridget = getRidget();
		MenuItem widget = ridget.getUIControl();

		ridget.setIcon(ICON_ECLIPSE);

		assertEquals(ICON_ECLIPSE, ridget.getIcon());
		assertNotNull(widget.getImage());

		ridget.setIcon(null);

		assertNull(ridget.getIcon());
		assertNull(widget.getImage());

		MenuItem button = createWidget(getShell());
		Image buttonImage = button.getDisplay().getSystemImage(SWT.ICON_INFORMATION);
		button.setImage(buttonImage);
		IActionRidget buttonRidget = createRidget();
		// binding doesn't remove image of button, because the icon of the ridget is null and the method #setIcon wasn't called yet.
		buttonRidget.setUIControl(button);
		assertSame(buttonImage, button.getImage());

		buttonRidget.setIcon(null);
		assertNull(buttonRidget.getIcon());
		assertNull(button.getImage());

		buttonRidget.setIcon(ICON_ECLIPSE);
		assertEquals(ICON_ECLIPSE, buttonRidget.getIcon());
		assertNotNull(button.getImage());
		assertNotSame(buttonImage, button.getImage());

		button = createWidget(getShell());
		button.setImage(buttonImage);
		buttonRidget = createRidget();
		buttonRidget.setIcon(ICON_ECLIPSE);
		// binding replaces image of button, because the icon of the ridget is not null.
		buttonRidget.setUIControl(button);
		assertNotNull(button.getImage());
		assertNotSame(buttonImage, button.getImage());

	}

	/**
	 * Tests the method {@code initText}
	 */
	public void testInitText() {
		MenuItemRidget ridget = getRidget();
		MenuItem widget = ridget.getUIControl();

		ReflectionUtils.setHidden(ridget, "textAlreadyInitialized", false);
		ReflectionUtils.setHidden(ridget, "text", null);
		widget.setText("Hello!");

		ReflectionUtils.invokeHidden(ridget, "initText", new Object[] {});
		assertEquals("Hello!", ridget.getText());
		assertEquals("Hello!", widget.getText());
		assertTrue((Boolean) ReflectionUtils.getHidden(ridget, "textAlreadyInitialized"));

		widget.setText("World");
		ReflectionUtils.invokeHidden(ridget, "initText", new Object[] {});
		assertEquals("Hello!", ridget.getText());
		assertEquals("World", widget.getText());
	}

	public void testAddListener() {
		MenuItem widget = getWidget();
		MenuItemRidget ridget = getRidget();

		FTActionListener listener1 = new FTActionListener();
		FTActionListener listener2 = new FTActionListener();

		ridget.addListener(listener1);
		ridget.addListener(listener2);
		// listener2 will not be added again
		// if the same instance is already added
		ridget.addListener(listener2);

		fireSelectionEvent(widget);

		assertEquals(1, listener1.getCount());
		assertEquals(1, listener2.getCount());

		ridget.removeListener(listener1);
		fireSelectionEvent(widget);

		assertEquals(1, listener1.getCount());
		assertEquals(2, listener2.getCount());

		ridget.removeListener(listener2);
		fireSelectionEvent(widget);

		assertEquals(1, listener1.getCount());
		assertEquals(2, listener2.getCount());

		ridget.removeListener(listener2);
		fireSelectionEvent(widget);

		assertEquals(1, listener1.getCount());
		assertEquals(2, listener2.getCount());
	}

	// helping methods
	// ////////////////

	private void fireSelectionEvent(MenuItem control) {
		Event event = new Event();
		event.type = SWT.Selection;
		event.widget = control;
		control.notifyListeners(SWT.Selection, event);
	}

	private static final class FTActionListener implements IActionListener {
		private int count;

		public void callback() {
			count++;
		}

		int getCount() {
			return count;
		}

	}

}
