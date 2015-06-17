package com.fredhopper.server;

import javax.ws.rs.core.Application;

import com.fredhopper.environment.Environment;
import com.fredhopper.server.spi.ApplicationFactory;

class TestApplicationFactory implements ApplicationFactory {

  @Override
  public Application createApplication(Environment environment) {
    return new TestApplicationResourceConfig();
  }

  @Override
  public String path() {
    return "test-app";
  }

}
