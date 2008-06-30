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
package org.eclipse.riena.ui.ridgets.marker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.riena.ui.core.marker.IMessageMarker;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IStatusbarRidget;
import org.eclipse.riena.ui.ridgets.listener.FocusEvent;
import org.eclipse.riena.ui.ridgets.listener.IFocusListener;

/**
 * Visualizes certain types of message markers by displaying the message in the
 * statusbar.
 */
public class StatusbarMessageMarkerViewer extends AbstractMessageMarkerViewer {

	private String statusbarMessage;
	private String originalStatusbarMessage;
	private IStatusbarRidget statusbar = null;

	private PropertyChangeListener markerPropertyChangeListener = new MarkerPropertyChangeListener();
	private IFocusListener ridgetFocusListener = new RidgetFocusListener();

	/**
	 * @param statusbarRidget
	 *            The statusbar.
	 */
	public StatusbarMessageMarkerViewer(IStatusbarRidget statusbarRidget) {
		this.statusbar = statusbarRidget;
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.marker.AbstractMessageMarkerViewer#addRidget(org.eclipse.riena.ui.ridgets.IMarkableRidget)
	 */
	@Override
	public void addRidget(IMarkableRidget markableRidget) {
		super.addRidget(markableRidget);
		markableRidget.addPropertyChangeListener(markerPropertyChangeListener);
		markableRidget.addFocusListener(ridgetFocusListener);
	}

	protected void showMessages(IMarkableRidget markableRidget) {
		if (markableRidget.hasFocus()) {
			Collection messageMarker = this.getMessageMarker(markableRidget);
			String message = constructMessage(messageMarker).trim();
			// show the message only if there is something to show
			if (message.length() > 0 && isVisible()) {
				setStatusbarMessage(message);
			} else {
				hideMessages(markableRidget);
			}
		}
	}

	protected void hideMessages(IMarkableRidget ridget) {
		if (ridget.hasFocus()) {
			resetStatusbarMessage();
		}
	}

	private void setStatusbarMessage(String message) {
		if (getStatusBar() != null) {
			if (statusbarMessage == null) {
				originalStatusbarMessage = getStatusBar().getMessage();
			}
			this.getStatusBar().setMessage(message);
			statusbarMessage = message;
		}
	}

	private void resetStatusbarMessage() {
		if (getStatusBar() != null) {
			if (statusbarMessage != null && statusbarMessage.equals(getStatusBar().getMessage())) {
				this.getStatusBar().setMessage(originalStatusbarMessage);
			}
			statusbarMessage = null;
		}
	}

	private String constructMessage(Collection messageMarker) {
		StringWriter sw = new StringWriter();
		IMessageMarker nextMarker = null;
		if (messageMarker != null) {
			for (Iterator i = messageMarker.iterator(); i.hasNext();) {
				nextMarker = (IMessageMarker) i.next();
				if (sw.toString().trim().length() > 0) {
					sw.write(" ");
				}
				if (nextMarker.getMessage() != null) {
					sw.write(nextMarker.getMessage());
				}
			}
		}
		return sw.toString().trim();
	}

	IStatusbarRidget getStatusBar() {
		// if ( statusbar == null ) {
		// IModuleApplicationController moduleApplicationController =
		// subModuleController.getModuleApplicationController();
		// if ( moduleApplicationController != null ) {
		// statusbar = moduleApplicationController.getStatusBar();
		// PostCondition.assertNotNull( "The statusbar to show messages in must
		// not be
		// null!", statusbar );
		// }
		// }
		return statusbar;
	}

	private class MarkerPropertyChangeListener implements PropertyChangeListener {

		/**
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(IMarkableRidget.PROPERTY_MARKER)
					&& evt.getSource() instanceof IMarkableRidget && ((IMarkableRidget) evt.getSource()).hasFocus()) {
				showMessages((IMarkableRidget) evt.getSource());
			}
		}

	}

	private class RidgetFocusListener implements IFocusListener {

		/**
		 * @see org.eclipse.riena.ui.ridgets.listener.IFocusListener#focusGained(org.eclipse.riena.ui.ridgets.listener.FocusEvent)
		 */
		public void focusGained(FocusEvent event) {
			if (event.getNewFocusOwner() instanceof IMarkableRidget) {
				showMessages((IMarkableRidget) event.getNewFocusOwner());
			}
		}

		/**
		 * @see org.eclipse.riena.ui.ridgets.listener.IFocusListener#focusLost(org.eclipse.riena.ui.ridgets.listener.FocusEvent)
		 */
		public void focusLost(FocusEvent event) {
			if (event.getOldFocusOwner() instanceof IMarkableRidget) {
				resetStatusbarMessage();
			}
		}

	}

}
