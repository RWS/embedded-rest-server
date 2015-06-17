package com.fredhopper.server;

import static com.google.common.truth.Truth.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.junit.Before;
import org.junit.Test;

import com.fredhopper.environment.Environment;
import com.google.common.base.StandardSystemProperty;

public class EmbeddedServerTest {

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
  public void createServerWithNoHandlersContainsNoHandler() throws Exception {
    EmbeddedServer embs = new EmbeddedServer(env);
    embs.initLifeCycle();
    Handler handler = embs.getServer().getHandler();
    assertThat(handler).isNotNull();
    assertThat(handler).isInstanceOf(HandlerCollection.class);
    HandlerCollection hc = (HandlerCollection) handler;
    assertThat(hc.getHandlers()).isNull();
  }

}
