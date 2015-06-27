package com.fredhopper.server;

import javax.ws.rs.core.Application;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * An extension of Jetty {@link ServletContextHandler} that
 * specifically holds and serves an instance of JAX-RS
 * {@link Application} using Jersey {@link ServletContainer}.
 */
public class JerseyServletContextHandler extends ServletContextHandler {

  /**
   * Ctor.
   * 
   * @see #JerseyServletContextHandler(Application, String,
   *      String)
   * @param application the JAX-RS {@link Application}
   * @param contextPath the context path for this servlet
   *        context
   */
  public JerseyServletContextHandler(Application application, String contextPath) {
    this(application, contextPath, "/*");
  }

  /**
   * Ctor.
   * 
   * @param application the JAX-RS {@link Application}
   * @param contextPath the context path for this servlet
   *        context
   * @param pathSpec the path pattern to which the JAX-RS
   *        application should serve requests; e.g.
   *        <code>/*</code>or <code>/app/spec/*</code>
   */
  public JerseyServletContextHandler(Application application, String contextPath, String pathSpec) {
    setContextPath(contextPath);
    ResourceConfig resourceConfig = ResourceConfig.forApplication(application);
    ServletContainer servletContainer = new ServletContainer(resourceConfig);
    addServlet(new ServletHolder(toString(application), servletContainer), pathSpec);
  }

  private String toString(Application application) {
    return application.getClass().getSimpleName() + "@"
        + Integer.toHexString(application.hashCode());
  }

}
