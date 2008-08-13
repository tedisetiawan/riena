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
package org.eclipse.riena.core.util;

import java.util.EventListener;

import org.eclipse.riena.tests.RienaTestCase;

/**
 * Tests the <code>ListenerList</code>
 */
public class ListenerListTest extends RienaTestCase {

	private ListenerList<TestListener> listenerList = new ListenerList<TestListener>(TestListener.class);

	public void testFresh() {
		EventListener[] list = listenerList.getListeners();
		assertNotNull(list);
		assertEquals(0, list.length);
		TestListener.pieps = 0;
		for (TestListener listener : listenerList.getListeners()) {
			listener.piep();
		}
		assertEquals(0, TestListener.pieps);
	}

	public void testAddOneRemoveOne() {
		EventListener[] list = listenerList.getListeners();
		assertNotNull(list);
		assertEquals(0, list.length);
		TestListener t = new TestListener();
		listenerList.add(t);
		assertEquals(1, listenerList.getListeners().length);
		TestListener.pieps = 0;
		for (TestListener listener : listenerList.getListeners()) {
			listener.piep();
		}
		assertEquals(listenerList.getListeners().length, TestListener.pieps);

		listenerList.remove(t);
		assertEquals(0, listenerList.getListeners().length);
	}

	public void testAddMoreRemoveAll() {
		EventListener[] list = listenerList.getListeners();
		assertNotNull(list);
		assertEquals(0, list.length);
		TestListener[] listeners = new TestListener[10];
		for (int i = 0; i < listeners.length; i++) {
			listeners[i] = new TestListener();
			listenerList.add(listeners[i]);
			TestListener.pieps = 0;
			for (TestListener listener : listenerList.getListeners()) {
				listener.piep();
			}
			assertEquals(listenerList.getListeners().length, TestListener.pieps);
		}
		for (int i = listeners.length - 1; i >= 0; i--) {
			listenerList.remove(listeners[i]);
			assertEquals(i, listenerList.getListeners().length);
			TestListener.pieps = 0;
			for (TestListener listener : listenerList.getListeners()) {
				listener.piep();
			}
			assertEquals(listenerList.getListeners().length, TestListener.pieps);
		}
		assertEquals(0, listenerList.getListeners().length);
	}

	private static class TestListener implements EventListener {
		public static int pieps;

		public void piep() {
			pieps++;
		}
	}
}
