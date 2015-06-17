package com.fredhopper.server.spi;

import java.util.ServiceLoader;

import com.fredhopper.environment.Environment;

/**
 * Provides a hook in the initial phase of booting the server to
 * apply any logging configurations and setup. <b>Note</b> that:
 * <ul>
 * <li>Implementations are identified through
 * {@link ServiceLoader}.
 * <li>The implementation is used in a <code>static</code>
 * context and just <b>once</b>.
 * </ul>
 */
public interface LoggingConfigurator {

  /**
   * Applies any logic to configure or set up logging API and
   * runtime for the application.
   * 
   * @param environment the {@link Environment} of the
   *        application/server instance
   */
  void configure(Environment environment);

}
