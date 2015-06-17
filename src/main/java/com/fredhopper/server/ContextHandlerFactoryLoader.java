package com.fredhopper.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;
import java.util.function.Function;

import org.eclipse.jetty.server.handler.ContextHandler;

import com.fredhopper.environment.Environment;
import com.fredhopper.server.spi.ContextHandlerFactory;

/**
 * Loads all the {@link ContextHandlerFactory} implementations
 * using {@link ServiceLoader} from the current class path.
 */
class ContextHandlerFactoryLoader implements Function<Environment, Collection<ContextHandler>> {

  @Override
  public Collection<ContextHandler> apply(Environment environment) {
    Iterable<ContextHandlerFactory> loader = loadFactories();
    Collection<ContextHandler> handlers = new ArrayList<>();
    for (ContextHandlerFactory factory : loader) {
      ContextHandler contextHandler = createContextHandler(factory, environment);
      if (contextHandler != null) {
        handlers.add(contextHandler);
      }
    }
    return handlers;
  }

  /**
   * The collection of loaded factories.
   * 
   * @return an instance of {@link ServiceLoader} over
   *         {@link ContextHandlerFactory}.
   */
  protected Iterable<ContextHandlerFactory> loadFactories() {
    return ServiceLoader.<ContextHandlerFactory>load(ContextHandlerFactory.class);
  }

  /**
   * See {@link ContextHandlerFactory}.
   * 
   * @param factory the context handler factory
   * @param environment the environment
   * @return an instance {@link ContextHandler}
   */
  protected ContextHandler createContextHandler(ContextHandlerFactory factory,
      Environment environment) {
    return factory.createContextHandler(environment);
  }

}
