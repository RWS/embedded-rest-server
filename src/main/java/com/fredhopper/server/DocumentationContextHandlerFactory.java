package com.fredhopper.server;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;

import com.fredhopper.environment.Environment;
import com.fredhopper.server.spi.ContextHandlerFactory;

/**
 * A {@link ContextHandlerFactory} that creates a context
 * located at <code>/docs</code> if the necessary elements are
 * available in the provided environment.
 */
public class DocumentationContextHandlerFactory implements ContextHandlerFactory {

  /**
   * Default name of the folder inside application root to hold
   * documentation.
   */
  private static final String DEFAULT_DOCS_FOLDER = "docs";
  /**
   * The default context path of the documentation.
   */
  private static final String CONTEXT_PATH = "/" + DEFAULT_DOCS_FOLDER;

  @Override
  public ContextHandler createContextHandler(Environment environment) {
    String appName = environment.getApplicationName();
    String docsRootPath = environment.getValue(appName + ".docs.root");
    if (docsRootPath != null && containsDocumentation(Paths.get(docsRootPath))) {
      return createDocumentationContext(Paths.get(docsRootPath));
    }
    Path root = environment.getApplicationRoot();
    if (root != null && containsDocumentation(root.resolve(DEFAULT_DOCS_FOLDER))) {
      return createDocumentationContext(root.resolve(DEFAULT_DOCS_FOLDER));
    }
    return null;
  }

  /**
   * Creates a {@link ContextHandler} with
   * {@link ResourceHandler} using the root of documentation.
   * 
   * @param docsRoot the path to the documentation root
   * @return an instance {@link ContextHandler}
   */
  protected ContextHandler createDocumentationContext(Path docsRoot) {
    ContextHandler contextHandler = new ContextHandler();
    contextHandler.setContextPath(CONTEXT_PATH);
    ResourceHandler resourceHandler = new ResourceHandler();
    resourceHandler.setBaseResource(Resource.newResource(docsRoot.toFile()));
    contextHandler.setHandler(resourceHandler);
    return contextHandler;
  }

  /**
   * Checks if the documentation root path contains actual
   * documentation.
   * 
   * @param docsRoot the documentation root path
   * @return <code>true</code> if the documentation default
   *         <code>index.html</code> is readable in the
   *         documentation root path; otherwise
   *         <code>false</code>
   */
  protected boolean containsDocumentation(Path docsRoot) {
    return Files.exists(docsRoot) && Files.isReadable(docsRoot.resolve("index.html"));
  }

}
