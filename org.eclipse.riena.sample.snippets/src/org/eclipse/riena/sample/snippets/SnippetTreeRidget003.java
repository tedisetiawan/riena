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
package org.eclipse.riena.sample.snippets;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.ridgets.tree2.TreeNode;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Hiding the root element in a TreeRidget.
 */
public class SnippetTreeRidget003 {

	public SnippetTreeRidget003(final Shell shell) {

		final Tree tree = new Tree(shell, SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(tree);

		final ITreeRidget treeRidget = (ITreeRidget) SwtRidgetFactory.createRidget(tree);
		final TreeNode[] roots = createTreeInput();
		treeRidget.bindToModel(roots, TreeNode.class, TreeNode.PROPERTY_CHILDREN, TreeNode.PROPERTY_PARENT,
				TreeNode.PROPERTY_VALUE);

		final Button button = UIControlsFactory.createButtonCheck(shell);
		GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).applyTo(button);
		button.setText("show &root"); //$NON-NLS-1$
		button.setSelection(true);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				treeRidget.setRootsVisible(button.getSelection());
				treeRidget.updateFromModel();
				if (button.getSelection()) {
					treeRidget.expand(roots[0]);
				}
			}
		});
	}

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
			GridLayoutFactory.swtDefaults().applyTo(shell);
			new SnippetTreeRidget003(shell);
			shell.setSize(200, 200);
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} finally {
			display.dispose();
		}
	}

	private TreeNode[] createTreeInput() {

		final TreeNode root = new TreeNode("Gods - root of tree"); //$NON-NLS-1$

		final TreeNode greek = new TreeNode(root, "Greek Gods"); //$NON-NLS-1$
		new TreeNode(greek, "Aphrodite"); //$NON-NLS-1$
		new TreeNode(greek, "Apollo"); //$NON-NLS-1$
		new TreeNode(greek, "Ares"); //$NON-NLS-1$
		new TreeNode(greek, "Artemis"); //$NON-NLS-1$
		new TreeNode(greek, "Athena"); //$NON-NLS-1$
		new TreeNode(greek, "Demeter"); //$NON-NLS-1$
		new TreeNode(greek, "Dionysus"); //$NON-NLS-1$
		new TreeNode(greek, "Hephaestus"); //$NON-NLS-1$
		new TreeNode(greek, "Hera"); //$NON-NLS-1$
		new TreeNode(greek, "Hermes"); //$NON-NLS-1$
		new TreeNode(greek, "Hestia"); //$NON-NLS-1$
		new TreeNode(greek, "Zeus"); //$NON-NLS-1$
		final TreeNode greekDemigods = new TreeNode(greek, "Demigods"); //$NON-NLS-1$
		new TreeNode(greekDemigods, "Achilles"); //$NON-NLS-1$
		new TreeNode(greekDemigods, "Hercules"); //$NON-NLS-1$
		new TreeNode(greekDemigods, "Perseus "); //$NON-NLS-1$

		final TreeNode roman = new TreeNode(root, "Roman Gods"); //$NON-NLS-1$
		new TreeNode(roman, "Diana"); //$NON-NLS-1$
		new TreeNode(roman, "Janus"); //$NON-NLS-1$
		new TreeNode(roman, "Juno"); //$NON-NLS-1$
		new TreeNode(roman, "Jupiter"); //$NON-NLS-1$
		new TreeNode(roman, "Mars"); //$NON-NLS-1$
		new TreeNode(roman, "Saturn"); //$NON-NLS-1$
		new TreeNode(roman, "Vesta"); //$NON-NLS-1$

		final TreeNode germanic = new TreeNode(root, "Germanic Gods"); //$NON-NLS-1$
		new TreeNode(germanic, "Thor"); //$NON-NLS-1$
		new TreeNode(germanic, "Odin"); //$NON-NLS-1$
		new TreeNode(germanic, "Tyr"); //$NON-NLS-1$
		new TreeNode(germanic, "Frigg"); //$NON-NLS-1$

		return new TreeNode[] { root };
	}

}
