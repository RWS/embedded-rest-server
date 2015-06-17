package com.fredhopper.server;

import static com.google.common.truth.Truth.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.jetty.server.handler.ShutdownHandler;
import org.junit.Before;
import org.junit.Test;

import com.fredhopper.environment.Environment;
import com.google.common.base.StandardSystemProperty;

public class HandlerCollectionLoaderTest {

  private Environment env;
  private Map<String, String> defaultEnv;

  @Before
  public void before() throws Exception {
    defaultEnv = new HashMap<>();
    String name = UUID.randomUUID().toString();
    defaultEnv.put(Environment.APPLICATION_NAME, name);
    defaultEnv.put(name + Environment.ROOT_SUFFIX, StandardSystemProperty.JAVA_IO_TMPDIR.value());
    defaultEnv.put(name + Environment.SERVER_HOST_SUFFIX, "localhost");
    defaultEnv.put(name + Environment.SERVER_PORT_SUFFIX, "7777");
    env = Environment.createEnvironment(defaultEnv);
  }

  @Test
  public void createShutdownHandlerReturnsNullWithNoToken() throws Exception {
    HandlerCollectionLoader hcl = new HandlerCollectionLoader();
    ShutdownHandler shutdownHandler = hcl.createShutdownHandler(env);
    assertThat(shutdownHandler).isNull();
  }
  
  @Test
  public void createShutdownHandlerObserversShutdownTokenWhenPresent() throws Exception {
    HandlerCollectionLoader hcl = new HandlerCollectionLoader();
    String token = UUID.randomUUID().toString();
    defaultEnv.put(Environment.SERVER_SHUTDOWN_TOKEN_KEY, token);
    env = Environment.createEnvironment(defaultEnv);
    ShutdownHandler shutdownHandler = hcl.createShutdownHandler(env);
    assertThat(shutdownHandler).isNotNull();
    assertThat(shutdownHandler.getShutdownToken()).isEqualTo(token);
  }

}
