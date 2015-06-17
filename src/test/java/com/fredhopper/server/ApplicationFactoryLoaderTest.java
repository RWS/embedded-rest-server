package com.fredhopper.server;

import static com.google.common.truth.Truth.assertThat;

import java.util.Collection;
import java.util.Collections;

import javax.ws.rs.core.Application;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Test;

import com.fredhopper.environment.Environment;
import com.fredhopper.server.spi.ApplicationFactory;

public class ApplicationFactoryLoaderTest {

  private static class NullApplicationFactory implements ApplicationFactory {
    @Override
    public Application createApplication(Environment environment) {
      return null;
    }

    @Override
    public String path() {
      return null;
    }
  }

  @Test
  public void createApplicationUsesProvidedFactories() throws Exception {
    ApplicationFactoryLoader loader = new ApplicationFactoryLoader() {
      @Override
      protected Iterable<ApplicationFactory> loadFactories() {
        return Collections.singleton(new TestApplicationFactory());
      }
    };
    Collection<ServletContextHandler> contexts = loader.apply(null);
    assertThat(contexts).isNotNull();
    assertThat(contexts).isNotEmpty();
    assertThat(contexts).hasSize(1);
  }

  @Test
  public void createApplicationsReturnsEmptyOnNullApplication() throws Exception {
    ApplicationFactoryLoader loader = new ApplicationFactoryLoader() {
      @Override
      protected Iterable<ApplicationFactory> loadFactories() {
        return Collections.singleton(new NullApplicationFactory());
      }
    };
    Collection<ServletContextHandler> contexts = loader.apply(null);
    assertThat(contexts).isNotNull();
    assertThat(contexts).isEmpty();
  }

}
