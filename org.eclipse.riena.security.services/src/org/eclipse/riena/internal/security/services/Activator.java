package org.eclipse.riena.internal.security.services;

import java.util.Hashtable;

import org.eclipse.riena.core.RienaActivator;
import org.eclipse.riena.core.service.ServiceDescriptor;
import org.eclipse.riena.internal.security.authenticationservice.AuthenticationService;
import org.eclipse.riena.internal.security.authorizationservice.AuthorizationService;
import org.eclipse.riena.internal.security.sessionservice.SessionService;
import org.eclipse.riena.security.common.authentication.IAuthenticationService;
import org.eclipse.riena.security.common.authorization.IAuthorizationService;
import org.eclipse.riena.security.server.session.ISessionService;
import org.eclipse.riena.security.sessionservice.ISessionProvider;
import org.eclipse.riena.security.sessionservice.SessionProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends RienaActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.security.services";

	private ServiceRegistration authenticationService;
	private ServiceRegistration sessionService;
	private ServiceRegistration sessionProvider;
	private ServiceRegistration authorizationService;

	/**
	 * The constructor
	 */
	public Activator() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		// register AuthenticationService
		Hashtable<String, Object> properties = ServiceDescriptor.newDefaultServiceProperties();
		properties.put("riena.remote", Boolean.TRUE.toString());
		properties.put("riena.remote.protocol", "hessian");
		properties.put("riena.remote.path", IAuthenticationService.WS_ID);
		authenticationService = getContext().registerService(IAuthenticationService.class.getName(),
				new AuthenticationService(), properties);

		// register SessionService
		properties = ServiceDescriptor.newDefaultServiceProperties();
		properties.put("riena.remote", Boolean.TRUE.toString());
		properties.put("riena.remote.protocol", "hessian");
		properties.put("riena.remote.path", ISessionService.WS_ID);
		sessionService = getContext()
				.registerService(ISessionService.class.getName(), new SessionService(), properties);

		// register AuthorizationService
		properties = ServiceDescriptor.newDefaultServiceProperties();
		properties.put("riena.remote", Boolean.TRUE.toString());
		properties.put("riena.remote.protocol", "hessian");
		properties.put("riena.remote.path", IAuthorizationService.WS_ID);
		authorizationService = getContext().registerService(IAuthorizationService.class.getName(),
				new AuthorizationService(), properties);

		// register SessionProvider
		sessionProvider = getContext().registerService(ISessionProvider.class.getName(), new SessionProvider(),
				ServiceDescriptor.newDefaultServiceProperties());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		authenticationService.unregister();
		authorizationService.unregister();
		sessionService.unregister();
		sessionProvider.unregister();
		super.stop(context);
	}

}
