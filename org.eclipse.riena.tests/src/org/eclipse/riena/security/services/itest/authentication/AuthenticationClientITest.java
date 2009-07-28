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
package org.eclipse.riena.security.services.itest.authentication;

import java.security.Principal;
import java.util.Arrays;

import javax.security.auth.Subject;

import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.service.Service;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.security.common.ISubjectHolder;
import org.eclipse.riena.security.common.authentication.AuthenticationFailure;
import org.eclipse.riena.security.common.authentication.AuthenticationTicket;
import org.eclipse.riena.security.common.authentication.IAuthenticationService;
import org.eclipse.riena.security.common.authentication.credentials.AbstractCredential;
import org.eclipse.riena.security.common.authentication.credentials.NameCredential;
import org.eclipse.riena.security.common.authentication.credentials.PasswordCredential;
import org.eclipse.riena.security.server.session.ISessionService;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.IntegrationTestCase;

/**
 * Test client for authentication service.
 * 
 */
@IntegrationTestCase
public class AuthenticationClientITest extends RienaTestCase {

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), AuthenticationClientITest.class);
	private IRemoteServiceRegistration sessionServiceRegistration;
	private IRemoteServiceRegistration authenticationServiceRegistration;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		startBundles("org\\.eclipse\\.equinox\\.cm.*", null);
		startBundles("org\\.eclipse\\.equinox\\.log.*", null);
		startBundles("org\\.eclipse\\.riena.communication.core", null);
		startBundles("org\\.eclipse\\.riena.communication.factory.hessian", null);
		sessionServiceRegistration = Register.remoteProxy(ISessionService.class).usingUrl(
				"http://localhost:8080/hessian/SessionService").withProtocol("hessian").andStart(
				Activator.getDefault().getContext());
		authenticationServiceRegistration = Register.remoteProxy(IAuthenticationService.class).usingUrl(
				"http://localhost:8080/hessian/AuthenticationService").withProtocol("hessian").andStart(
				Activator.getDefault().getContext());

	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		sessionServiceRegistration.unregister();
		authenticationServiceRegistration.unregister();
	}

	/**
	 * org.eclipse.riena.tests the webservice call to authentication service
	 * with typeMapping
	 * 
	 * @throws Exception
	 */
	public void testLogin() throws Exception {
		trace("Looking up Authentication Service...: ");

		ServiceReference ref = getContext().getServiceReference(IAuthenticationService.class.getName());
		IAuthenticationService authenticationService = (IAuthenticationService) getContext().getService(ref);

		trace("Service looked up: " + authenticationService.getClass().getName());

		AbstractCredential[] creds = new AbstractCredential[2];
		NameCredential nc = new NameCredential("username: ", "xx");
		nc.setName("testuser1");
		creds[0] = nc;
		PasswordCredential pc = new PasswordCredential("password: ", false);
		pc.setPassword("testpass2".toCharArray());
		creds[1] = pc;
		trace("Add credential: " + Arrays.toString(creds));

		AuthenticationTicket ticket = authenticationService.login("CentralSecurity", creds);

		trace("Return from login() - ticket: " + ticket);

		assertNotNull(ticket);
		assertNotNull(ticket.getSession());
		assertNotNull(ticket.getPrincipals());

		trace("Login successful - ticket: " + ticket);

		// sign off
		authenticationService.logout();

		trace("Logoff successful.");
	}

	public void testInvalidLogin() throws Exception {

		try {
			ServiceReference ref = getContext().getServiceReference(IAuthenticationService.class.getName());
			IAuthenticationService authenticationService = (IAuthenticationService) getContext().getService(ref);
			AbstractCredential[] creds = new AbstractCredential[2];
			NameCredential nc = new NameCredential("username: ", "xx");
			nc.setName("john");
			creds[0] = nc;
			PasswordCredential pc = new PasswordCredential("password: ", false);
			pc.setPassword("jane".toCharArray());
			creds[1] = pc;
			authenticationService.login("Test", creds);
			fail("exception expected");
		} catch (AuthenticationFailure e) {
			ok("exception expected");
		}
	}

	public void testSubjectLogin() throws Exception {
		ServiceReference ref = getContext().getServiceReference(IAuthenticationService.class.getName());
		IAuthenticationService authenticationService = (IAuthenticationService) getContext().getService(ref);

		trace("Service looked up: " + authenticationService.getClass().getName());

		AbstractCredential[] creds = new AbstractCredential[2];
		NameCredential nc = new NameCredential("username: ", "xx");
		nc.setName("testuser1");
		creds[0] = nc;
		PasswordCredential pc = new PasswordCredential("password: ", false);
		pc.setPassword("testpass2".toCharArray());
		creds[1] = pc;

		AuthenticationTicket ticket = authenticationService.login("CentralSecurity", creds);

		trace("Return from login() - ticket: " + ticket);
		Subject subject = new Subject();
		for (Principal p : ticket.getPrincipals()) {
			subject.getPrincipals().add(p);
		}
		//		ServiceReference ref2 = getContext().getServiceReference(ISubjectHolderService.class.getName());
		//		ISubjectHolderService subHolderService = (ISubjectHolderService) getContext().getService(ref2);
		Service.get(ISubjectHolder.class).setSubject(subject);

		assertTrue(Service.get(ISubjectHolder.class).getSubject() == subject);
	}

	private void trace(String msg) {
		LOGGER.log(LogService.LOG_INFO, "|--->" + msg);
	}

}