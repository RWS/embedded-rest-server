package com.fredhopper.server;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ShutdownHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.fredhopper.environment.Environment;
import com.fredhopper.server.spi.ApplicationFactory;
import com.fredhopper.server.spi.ContextHandlerFactory;
import com.fredhopper.server.spi.RequestLogHandlerFactory;

/**
 * Collects all implementations of {@link ApplicationFactory},
 * {@link ContextHandlerFactory}, and
 * {@link RequestLogHandlerFactory}.
 */
class HandlerCollectionLoader implements Function<Environment, HandlerCollection> {

  @Override
  public HandlerCollection apply(Environment environment) {
    HandlerCollection handlers = new HandlerCollection();
    List<RequestLogHandler> requestLogHandlers = createRequestLogHandlers(environment);
    Collection<ServletContextHandler> servletContextHandlers = createServletContexts(environment);
    Collection<ContextHandler> contextHandlers = createContextHandlers(environment);

    ContextHandlerCollection contexts = new ContextHandlerCollection();
    addContextHandlers(contexts, servletContextHandlers);
    addContextHandlers(contexts, contextHandlers);
    if (contexts.getHandlers() != null && contexts.getHandlers().length > 0) {
      handlers.addHandler(contexts);
    }

    // Shutdown Handler of Jetty
    ShutdownHandler shutdownHandler = createShutdownHandler(environment);
    if (shutdownHandler != null) {
      handlers.addHandler(shutdownHandler);
    }

    // Log handlers are created the first but processed the
    // last. In case there is an access log handler, the HTTP
    // response codes should be correct.
    addHandlers(handlers, requestLogHandlers);

    return handlers;
  }

  /**
   * See {@link ApplicationFactoryLoader}.
   * 
   * @param environment the environment
   * @return the collection of loaded
   *         {@link ServletContextHandler}s
   */
  protected Collection<ServletContextHandler> createServletContexts(Environment environment) {
    ApplicationFactoryLoader afl = new ApplicationFactoryLoader();
    return afl.apply(environment);
  }

  /**
   * See {@link ContextHandlerFactoryLoader}.
   * 
   * @param environment the environment
   * @return a collection of loaded {@link ContextHandler}
   */
  protected Collection<ContextHandler> createContextHandlers(Environment environment) {
    ContextHandlerFactoryLoader chfl = new ContextHandlerFactoryLoader();
    return chfl.apply(environment);
  }

  /**
   * See {@link RequestLogHandlerFactoryLoader}.
   * 
   * @param environment the environment
   * @return the {@link List} of loaded
   *         {@link RequestLogHandler}s
   */
  protected List<RequestLogHandler> createRequestLogHandlers(Environment environment) {
    RequestLogHandlerFactoryLoader rlhfl = new RequestLogHandlerFactoryLoader();
    return rlhfl.apply(environment);
  }

  /**
   * Creates Jetty's {@link ShutdownHandler}.
   * 
   * @param environment the environment
   * @return an instance of {@link ShutdownHandler} if
   *         {@link Environment#SERVER_SHUTDOWN_TOKEN_KEY} is
   *         available; otherwise <code>null</code>
   */
  protected ShutdownHandler createShutdownHandler(Environment environment) {
    String shutdownToken = environment.getServerShutdownToken();
    if (shutdownToken == null || shutdownToken.isEmpty()) {
      return null;
    }
    return new ShutdownHandler(shutdownToken, true, false);
  }

  private void addHandlers(HandlerCollection parentHandler,
      Collection<? extends AbstractHandler> childHandlers) {
    if (!childHandlers.isEmpty()) {
      for (AbstractHandler h : childHandlers) {
        parentHandler.addHandler(h);
      }
    }
  }

  private void addContextHandlers(ContextHandlerCollection parentHandler,
      Collection<? extends ContextHandler> childContexts) {
    if (!childContexts.isEmpty()) {
      for (ContextHandler h : childContexts) {
        parentHandler.addHandler(h);
      }
    }
  }

}
