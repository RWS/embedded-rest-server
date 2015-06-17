package com.fredhopper.server;

import org.glassfish.jersey.server.ResourceConfig;

class TestApplicationResourceConfig extends ResourceConfig {

  public TestApplicationResourceConfig() {
    registerInstances(new TestApplication(System.currentTimeMillis()));
  }

}
