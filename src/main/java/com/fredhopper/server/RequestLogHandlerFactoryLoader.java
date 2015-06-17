package com.fredhopper.server;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Function;

import org.eclipse.jetty.server.handler.RequestLogHandler;

import com.fredhopper.environment.Environment;
import com.fredhopper.server.spi.RequestLogHandlerFactory;

/**
 * Loads all the {@link RequestLogHandlerFactory} using
 * {@link ServiceLoader} from the current class path. This is a
 * {@link Function} over {@link Environment} to {@link List} of
 * {@link RequestLogHandler}. List abstraction is chosen to
 * allow for ensuring specific order of log handling.
 */
class RequestLogHandlerFactoryLoader implements Function<Environment, List<RequestLogHandler>> {

  @Override
  public List<RequestLogHandler> apply(Environment environment) {
    Iterable<RequestLogHandlerFactory> loader = loadFactories();
    List<RequestLogHandler> handlers = new ArrayList<>();
    for (RequestLogHandlerFactory factory : loader) {
      RequestLogHandler requestLogHandler = createRequestLogHandler(factory, environment);
      if (requestLogHandler != null) {
        handlers.add(requestLogHandler);
      }
    }
    return handlers;
  }

  /**
   * The collection of loaded factories.
   * 
   * @return an instance {@link ServiceLoader} over
   *         {@link RequestLogHandlerFactory}
   */
  protected Iterable<RequestLogHandlerFactory> loadFactories() {
    return ServiceLoader.<RequestLogHandlerFactory>load(RequestLogHandlerFactory.class);
  }

  /**
   * See {@link RequestLogHandlerFactory}.
   * 
   * @param factory the request log handler factory
   * @param environment the environment
   * @return an instance of {@link RequestLogHandler}
   */
  protected RequestLogHandler createRequestLogHandler(RequestLogHandlerFactory factory,
      Environment environment) {
    return factory.createRequestLogHandler(environment);
  }

}
