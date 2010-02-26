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
package org.eclipse.riena.ui.core.uiprocess;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.ProgressProvider;

/**
 * A job can be presented by several instances of {@link ProgressProvider}. This
 * one delegates to those providers.
 */
public class ProgressProviderBridge extends ProgressProvider {

	private static ProgressProviderBridge instance;
	private IProgressVisualizerLocator visualizerLocator;
	private Map<Job, UIProcess> jobUiProcess;

	public ProgressProviderBridge() {
		jobUiProcess = Collections.synchronizedMap(new HashMap<Job, UIProcess>());
	}

	public static ProgressProviderBridge instance() {
		if (instance == null) {
			instance = new ProgressProviderBridge();
		}
		return instance;
	}

	public void setVisualizerFactory(IProgressVisualizerLocator visualizerLocator) {
		this.visualizerLocator = visualizerLocator;
	}

	@Override
	public IProgressMonitor createMonitor(Job job) {
		ProgressProvider provider = queryProgressProvider(job);
		return provider.createMonitor(job);
	}

	private ProgressProvider queryProgressProvider(Job job) {
		UIProcess uiprocess = jobUiProcess.get(job);
		Object context = getContext(job);
		if (uiprocess == null) {
			uiprocess = createDefaultUIProcess(job);
		}
		UICallbackDispatcher dispatcher = (UICallbackDispatcher) uiprocess.getAdapter(UICallbackDispatcher.class);
		dispatcher.addUIMonitor(visualizerLocator.getProgressVisualizer(context));
		return dispatcher;
	}

	private Object getContext(Job job) {
		return job.getProperty(UIProcess.PROPERTY_CONTEXT);
	}

	private UIProcess createDefaultUIProcess(Job job) {
		return new UIProcess(job);
	}

	public void registerMapping(Job job, UIProcess process) {
		jobUiProcess.put(job, process);
	}

	public void unregisterMapping(Job job) {
		jobUiProcess.remove(job);
	}
}
