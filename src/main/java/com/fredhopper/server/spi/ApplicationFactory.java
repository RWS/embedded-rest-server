package com.fredhopper.server.spi;

import java.util.ServiceLoader;

import javax.ws.rs.core.Application;

import com.fredhopper.environment.Environment;

/**
 * A factory pattern to create an instance of JAX-RS
 * {@link Application} provided an instance of
 * {@link Environment}. The implementations are identified
 * through {@link ServiceLoader}.
 */
public interface ApplicationFactory {

  /**
   * Creates an instance JAX-RS {@link Application}.
   * 
   * @param environment the {@link Environment} of this
   *        application server
   * @return the {@link Application} instance or
   *         <code>null</code> if no application can be created
   */
  Application createApplication(Environment environment);

  /**
   * Specifies the context path at which the created
   * {@link Application} at
   * {@link #createApplication(Environment)} should be
   * accessible. The implementations might be able to use
   * {@link Environment#getContextPath()}.
   * 
   * @return the context path of JAX-RS application
   */
  String path();

}
