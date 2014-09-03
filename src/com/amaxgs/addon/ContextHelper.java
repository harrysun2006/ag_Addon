package com.amaxgs.addon;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.NamingManager;

import org.apache.naming.ContextAccessController;
import org.apache.naming.ContextBindings;
import org.apache.naming.NamingContext;
import org.apache.naming.ResourceRef;
import org.apache.naming.factory.ResourceLinkFactory;

public class ContextHelper {

	private final static String ROOT_NAME = "root";

	private final static String RESOURCE_NAME = "name";

	private final static String RESOURCE_TYPE = "type";

	private static NamingContext rootCtx;

	private static Context compCtx;

	private static Context envCtx;

	static {
		init();
	}

	private ContextHelper() {
	}

	private synchronized static void init() {
		try {
			Object container = new Object();
			String name = ROOT_NAME;
			Hashtable contextEnv = new Hashtable();
			rootCtx = new NamingContext(contextEnv, "ROOT");
			ContextAccessController.setSecurityToken(name, container);
			ContextBindings.bindContext(container, rootCtx, container);
			ContextBindings.bindClassLoader(container, rootCtx, ClassLoader.getSystemClassLoader());
			ContextAccessController.setWritable(name, container);
			compCtx = rootCtx.createSubcontext("java:comp");
			envCtx = compCtx.createSubcontext("env");
			ResourceLinkFactory.setGlobalContext(rootCtx);
			ContextBindings.bindClassLoader(container, container, ContextHelper.class.getClassLoader());
			NamingManager.setInitialContextFactoryBuilder(ApplicationInitialContextFactoryBuilder.instance);
		} catch (NamingException ne) {
			ne.printStackTrace();
		}
	}

	public static void addResource(String name) throws IOException {
		Properties props = new Properties();
		InputStream is = ClassLoader.getSystemResourceAsStream(name);
		props.load(is);
		is.close();
		addResource(props);
	}

	public static void addResource(Hashtable resource) {
		// Create a reference to the resource.
		String name = (String) resource.get(RESOURCE_NAME);
		String type = (String) resource.get(RESOURCE_TYPE);
		Reference ref = new ResourceRef(type, null, null, null);
		// Adding the additional parameters, if any
		Iterator params = resource.keySet().iterator();
		while (params.hasNext()) {
			String paramName = (String) params.next();
			String paramValue = (String) resource.get(paramName);
			StringRefAddr refAddr = new StringRefAddr(paramName, paramValue);
			ref.add(refAddr);
		}
		try {
			createSubcontexts(envCtx, name);
			envCtx.bind(name, ref);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public static void addValue(String name, Object value) {
		// Binding the object to the appropriate name
		if (value != null) {
			try {
				createSubcontexts(envCtx, name);
				envCtx.bind(name, value);
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
	}

	private static void createSubcontexts(Context ctx, String name)
			throws NamingException {
		Context currentContext = ctx;
		StringTokenizer tokenizer = new StringTokenizer(name, "/");
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if ((!token.equals("")) && (tokenizer.hasMoreTokens())) {
				try {
					currentContext = currentContext.createSubcontext(token);
				} catch (NamingException e) {
					// Silent catch. Probably an object is already bound in
					// the context.
					currentContext = (Context) currentContext.lookup(token);
				}
			}
		}
	}

	private final static class ApplicationInitialContextFactoryBuilder implements InitialContextFactoryBuilder {
		private final static ApplicationInitialContextFactoryBuilder instance = new ApplicationInitialContextFactoryBuilder();

		private ApplicationInitialContextFactoryBuilder() {
		}

    public InitialContextFactory createInitialContextFactory(Hashtable environment) throws NamingException {
    	return ApplicationInitialContextFactory.instance;
    }
	}

	private final static class ApplicationInitialContextFactory implements InitialContextFactory {
		private final static ApplicationInitialContextFactory instance = new ApplicationInitialContextFactory();

		public Context getInitialContext(Hashtable environment) throws NamingException {
			return ContextHelper.rootCtx;
		}
	}
}
