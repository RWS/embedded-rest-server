package com.fredhopper.server;

import com.fredhopper.lifecycle.LifeCycle;

/**
 * The standard JVM "main" class to initiate and start an
 * {@link EmbeddedServer} that can be used either in executable
 * JAR archives or directory in a script. <b>Note</b> that this
 * main thread blocks on {@link LifeCycle#startLifeCycle()}
 * through {@link EmbeddedServer#doStartLifeCycle()} and awaits
 * until Jetty server is shut down.
 */
public class Bootstrap {

  public static void main(String[] args) throws Exception {
    final LifeCycle server = EmbeddedServer.createEmbeddedServer();
    server.initLifeCycle();
    server.startLifeCycle();
  }

}
