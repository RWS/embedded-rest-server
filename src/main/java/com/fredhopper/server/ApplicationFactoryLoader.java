package com.fredhopper.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;
import java.util.function.Function;

import javax.ws.rs.core.Application;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.fredhopper.environment.Environment;
import com.fredhopper.server.spi.ApplicationFactory;

/**
 * Loads all {@link ApplicationFactory} using
 * {@link ServiceLoader} from the current class path.
 */
class ApplicationFactoryLoader implements Function<Environment, Collection<ServletContextHandler>> {

  @Override
  public Collection<ServletContextHandler> apply(Environment environment) {
    Iterable<ApplicationFactory> loader = loadFactories();
    Collection<ServletContextHandler> handlers = new ArrayList<>();
    for (ApplicationFactory factory : loader) {
      Application application = createApplication(environment, factory);
      if (application == null) {
        continue;
      }
      ResourceConfig resourceConfig = ResourceConfig.forApplication(application);
      ServletContainer servletContainer = new ServletContainer(resourceConfig);
      ServletContextHandler contextHandler = new ServletContextHandler();
      contextHandler.setContextPath(factory.path());
      contextHandler.addServlet(new ServletHolder(servletContainer), "/*");
      handlers.add(contextHandler);
    }
    return handlers;
  }

  /**
   * The collection of loaded factories.
   * 
   * @return an instance of {@link ServiceLoader} over
   *         {@link ApplicationFactory}
   */
  protected Iterable<ApplicationFactory> loadFactories() {
    return ServiceLoader.<ApplicationFactory>load(ApplicationFactory.class);
  }


  /**
   * Creates an application using {@link ApplicationFactory}.
   * 
   * @param environment the environment
   * @param factory the application factory
   * @return the created application or might be
   *         <code>null</code>
   */
  protected Application createApplication(Environment environment, ApplicationFactory factory) {
    return factory.createApplication(environment);
  }

}
