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
package org.eclipse.riena.navigation.ui.swt.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.listener.ApplicationNodeListener;
import org.eclipse.riena.navigation.listener.SubApplicationNodeListener;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.SubApplicationSwitcherRenderer;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * Control to switch between sub-applications.
 */
public class SubApplicationSwitcherWidget extends Canvas {

	private List<SubApplicationItem> items;
	private TabSelector tabSelector;
	private MnemonicListener mnemonicListener;
	private PaintDelegation paintDelegation;
	private Control control;
	private ApplicationListener applicationListener;
	private SubApplicationListener subApplicationListener;

	/**
	 * Creates a new widget.
	 * 
	 * @param parent
	 *            a composite control which will be the parent of the new
	 *            instance
	 * @param style
	 *            the style of control to construct
	 * @param application
	 *            the node of the application
	 */
	public SubApplicationSwitcherWidget(Composite parent, int style, IApplicationNode application) {

		super(parent, style | SWT.DOUBLE_BUFFERED);
		control = this;
		items = new ArrayList<SubApplicationItem>();
		applicationListener = new ApplicationListener();
		subApplicationListener = new SubApplicationListener();
		registerItems(application);

		addListeners();

	}

	/**
	 * Adds listeners to the widget.
	 */
	private void addListeners() {
		mnemonicListener = new MnemonicListener();
		addTraverseListener(mnemonicListener);
		tabSelector = new TabSelector();
		addMouseListener(tabSelector);
		paintDelegation = new PaintDelegation();
		addPaintListener(paintDelegation);
	}

	/**
	 * Removes all the listeners form the widget.
	 */
	private void removeListeners() {
		removePaintListener(paintDelegation);
		removeMouseListener(tabSelector);
		removeTraverseListener(mnemonicListener);
	}

	/**
	 * Activates the Sub-Application of the given item.
	 * 
	 * @param item
	 *            item to activate
	 * @return {@code true} if the sub-application was activated; otherwise
	 *         {@code false}
	 */
	private boolean activateItem(SubApplicationItem item) {
		if (isTabEnabled(item)) {
			item.getSubApplicationNode().activate();
			redraw();
			return true;
		}
		return false;
	}

	/**
	 * This listener pay attention that this control is paint correct.
	 */
	private class PaintDelegation implements PaintListener {

		/**
		 * Passes the bounds of the parent and the sub-application items to the
		 * renderer and paints the widget.
		 * 
		 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
		 */
		public void paintControl(PaintEvent e) {
			GC gc = e.gc;
			getRenderer().setBounds(getParent().getBounds());
			getRenderer().setItems(getItems());
			getRenderer().paint(gc, control);
		}

	}

	/**
	 * After the selection of a sub-application it will be activated.
	 */
	private class TabSelector extends MouseAdapter {

		/**
		 * Activates the selected sub-application
		 * 
		 * @see org.eclipse.swt.events.MouseAdapter#mouseDown(org.eclipse.swt.events.MouseEvent)
		 */
		@Override
		public void mouseDown(MouseEvent e) {
			SubApplicationItem item = getItem(new Point(e.x, e.y));
			activateItem(item);
		}

	}

	/**
	 * After entering a mnemonic the sub-application it will be activated.
	 */
	private final class MnemonicListener implements TraverseListener {

		public void keyTraversed(TraverseEvent evt) {
			if (evt.detail == SWT.TRAVERSE_MNEMONIC) {
				SubApplicationItem item = getItem(evt.character);
				activateItem(item);
			}
		}

	}

	/**
	 * Returns the sub-application at the given point.
	 * 
	 * @param point
	 *            point over sub-application item
	 * @return module item; or null, if not item was found
	 */
	private SubApplicationItem getItem(Point point) {

		for (SubApplicationItem item : getItems()) {
			if (item.getBounds().contains(point)) {
				return item;
			}
		}

		return null;

	}

	/**
	 * Returns the sub-application with given mnemonic.
	 * 
	 * @param Mnemonic
	 *            -
	 * @return module item; or null, if not item was found
	 */
	private SubApplicationItem getItem(char mnemonic) {

		String mnemonicStrg = "&" + mnemonic; //$NON-NLS-1$
		mnemonicStrg = mnemonicStrg.toLowerCase();

		for (SubApplicationItem item : getItems()) {
			String label = item.getLabel();
			if (label != null) {
				label = label.toLowerCase();
				if (label.contains(mnemonicStrg)) {
					if (label.indexOf('&') == label.indexOf(mnemonicStrg)) {
						return item;
					}
				}
			}
		}

		return null;

	}

	/**
	 * Returns whether the given item is enabled and also visible.
	 * 
	 * @param item
	 *            sub-application item
	 * @return {@code true} if item is enabled and visible; otherwise false.
	 */
	private boolean isTabEnabled(SubApplicationItem item) {

		if (item == null) {
			return false;
		}
		if (!item.getMarkersOfType(HiddenMarker.class).isEmpty()) {
			return false;
		}
		if (!item.getMarkersOfType(DisabledMarker.class).isEmpty()) {
			return false;
		}

		return true;

	}

	/**
	 * Returns the list of every registered item of a sub-application.
	 * 
	 * @return list of items.
	 */
	private List<SubApplicationItem> getItems() {
		return items;
	}

	/**
	 * Creates for every sub-application of the given application an item and
	 * registers it.
	 * 
	 * @param applicationModel
	 *            model of the application
	 */
	private void registerItems(IApplicationNode applicationModel) {

		applicationModel.addListener(applicationListener);

		List<ISubApplicationNode> subApps = applicationModel.getChildren();
		for (ISubApplicationNode subApp : subApps) {
			registerSubApplication(subApp);
		}

	}

	private void registerSubApplication(ISubApplicationNode subApp) {
		subApp.addListener(subApplicationListener);
		SubApplicationItem item = new SubApplicationItem(this, subApp);
		item.setIcon(subApp.getIcon());
		item.setLabel(subApp.getLabel());
		getItems().add(item);
		IApplicationNode applicationNode = findApplicationNode(getItems());
		if (applicationNode != null) {
			orderItems(applicationNode);
		}

	}

	private IApplicationNode findApplicationNode(List<SubApplicationItem> items) {
		for (SubApplicationItem item : items) {
			ISubApplicationNode node = item.getSubApplicationNode();
			if (node.getParent() != null) {
				return (IApplicationNode) node.getParent();
			}
		}
		return null;
	}

	private void orderItems(IApplicationNode appNode) {
		Collections.sort(getItems(), new SubApplicationItemComparator(appNode));
	}

	private class SubApplicationItemComparator implements Comparator<SubApplicationItem> {

		private IApplicationNode appNode;

		public SubApplicationItemComparator(IApplicationNode appNode) {
			this.appNode = appNode;
		}

		public int compare(SubApplicationItem item1, SubApplicationItem item2) {
			return appNode.getIndexOfChild(item1.getSubApplicationNode()) < appNode.getIndexOfChild(item2
					.getSubApplicationNode()) ? -1 : 1;
		}

	}

	private void unregisterSubApplication(ISubApplicationNode subApp) {
		subApp.removeListener(subApplicationListener);
		SubApplicationItem itemToRemove = null;
		for (SubApplicationItem item : getItems()) {
			if (item.getSubApplicationNode().equals(subApp)) {
				itemToRemove = item;
				break;
			}
		}
		if (itemToRemove != null) {
			getItems().remove(itemToRemove);
		}
	}

	/**
	 * Returns the renderer of the switcher of the sub-applications.
	 * 
	 * @return renderer of switcher of sub-applications
	 */
	private SubApplicationSwitcherRenderer getRenderer() {
		return (SubApplicationSwitcherRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.SUB_APPLICATION_SWITCHER_RENDERER);
	}

	/**
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {
		removeListeners();
		super.dispose();
	}

	private final class SubApplicationListener extends SubApplicationNodeListener {

		@Override
		public void markerChanged(ISubApplicationNode source, IMarker marker) {
			redraw();
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#disposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void disposed(ISubApplicationNode source) {
			unregisterSubApplication(source);
			redraw();
		}
	}

	private final class ApplicationListener extends ApplicationNodeListener {

		@Override
		public void childRemoved(IApplicationNode source, ISubApplicationNode childRemoved) {
			unregisterSubApplication(childRemoved);
			redraw();
		}

		@Override
		public void childAdded(IApplicationNode source, ISubApplicationNode childAdded) {
			registerSubApplication(childAdded);
			redraw();
		}

	}

}
