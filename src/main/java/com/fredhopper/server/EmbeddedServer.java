package com.fredhopper.server;

import java.util.ServiceLoader;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.fredhopper.environment.Environment;
import com.fredhopper.lifecycle.AbstractLifeCycle;
import com.fredhopper.lifecycle.Container;
import com.fredhopper.lifecycle.LifeCycle;
import com.fredhopper.server.spi.LoggingConfigurator;

/**
 * A wrapper {@link LifeCycle} that manages an instance of
 * Jetty's {@link Server}. The server uses
 * {@link HandlerCollectionLoader} to load all the available
 * implementations on the class path and adds the result as the
 * default {@link Handler} of this server.
 * <p>
 * The {@link EmbeddedServer} can be created using
 * {@link #createEmbeddedServer()} or
 * {@link #createServer(Environment)}. This way it is ensured
 * that {@link LifeCycle} management of the server occurs in
 * separate threads.
 */
public class EmbeddedServer extends AbstractLifeCycle {

  /**
   * The single instance of {@link Environment} for all
   * instances of {@link EmbeddedServer}s in this JVM.
   */
  private static final Environment ENVIRONMENT;

  static {
    /*
     * 1. Load a common single instance of Environment. 2.
     * Configure logging via ServiceLoader
     */
    ENVIRONMENT = Environment.createEnvironment();
    ServiceLoader<LoggingConfigurator> loader =
        ServiceLoader.<LoggingConfigurator>load(LoggingConfigurator.class);
    for (LoggingConfigurator lc : loader) {
      lc.configure(ENVIRONMENT);
    }
  }

  /**
   * The prefix used by Jetty when logging HTTP requests; e.g.
   * <code>http-NNN</code>.
   */
  private static final String DEFAULT_JETTY_THREAD_NAME = "http";

  /**
   * Creates an instance {@link EmbeddedServer} using
   * {@link Environment#createEnvironment()}.
   * 
   * @return an instance {@link LifeCycle} wrapping around the
   *         server
   */
  public static LifeCycle createEmbeddedServer() {
    return createEmbeddedServer(ENVIRONMENT);
  }

  /**
   * Creates an instance of {@link EmbeddedServer}.
   * 
   * @param environment the environment of the server
   * @return an instance {@link LifeCycle} wrapping around the
   *         server
   */
  public static LifeCycle createEmbeddedServer(Environment environment) {
    EmbeddedServer server = new EmbeddedServer(environment);
    Container container = new Container(server, true, true);
    return container;
  }

  private final Server server;
  private final Environment environment;

  /**
   * Ctor
   * 
   * @param environment the environment of the server
   */
  public EmbeddedServer(Environment environment) {
    this.environment = environment;
    this.server = createServer(environment);
  }

  @Override
  protected void doInitLifeCycle() throws Exception {
    HandlerCollectionLoader handlers = new HandlerCollectionLoader();
    server.setHandler(handlers.apply(environment));
  }

  @Override
  protected void doStartLifeCycle() throws Exception {
    this.server.start();
  }

  @Override
  protected void doStopLifeCycle() throws Exception {
    this.server.stop();
  }

  /**
   * Creates an instance of Jetty's server using
   * {@link Environment}. The provided {@link Environment}
   * specifies the host ({@link Environment#getServerHost()})
   * and the port ({@link Environment#getServerPort()}) of the
   * server.
   * 
   * @param environment the environment of the server
   * @return the Jetty's {@link Server}
   */
  protected Server createServer(Environment environment) {
    final QueuedThreadPool qtp = new QueuedThreadPool(1024, 16, 30);
    qtp.setName(DEFAULT_JETTY_THREAD_NAME);
    final Server server = new Server(qtp);
    final ServerConnector connector = new ServerConnector(server);
    connector.setHost(environment.getServerHost());
    connector.setPort(environment.getServerPort());
    server.addConnector(connector);
    return server;
  }

  protected final Server getServer() {
    return server;
  }

}
