package com.fredhopper.server.spi;

import java.util.ServiceLoader;

import org.eclipse.jetty.server.handler.ContextHandler;

import com.fredhopper.environment.Environment;

/**
 * A factory pattern for Jetty's {@link ContextHandler}. The
 * implementations are identified through {@link ServiceLoader}.
 */
public interface ContextHandlerFactory {

  /**
   * Creates an instance of {@link ContextHandler} using the
   * provided {@link Environment}.
   * 
   * @param environment the environment of the embedded server
   * @return an instance of {@link ContextHandler} or
   *         <code>null</code> of no context can be created.
   */
  ContextHandler createContextHandler(Environment environment);

}
