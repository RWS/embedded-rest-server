package com.fredhopper.server.spi;

import java.util.ServiceLoader;

import org.eclipse.jetty.server.handler.RequestLogHandler;

import com.fredhopper.environment.Environment;

/**
 * A factory pattern for Jetty's {@link RequestLogHandler}. The
 * implementations are identified through {@link ServiceLoader}.
 */
public interface RequestLogHandlerFactory {

  /**
   * Creates an instance {@link RequestLogHandler} using the
   * provided environment.
   * 
   * @param environment the environment of the embedded server
   * @return an instance of {@link RequestLogHandler} or
   *         <code>null</code> if no request log handler can be
   *         created.
   */
  RequestLogHandler createRequestLogHandler(Environment environment);

}
