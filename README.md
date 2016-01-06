[![Build Status](https://img.shields.io/travis/sdl/embedded-rest-server.svg?style=flat-square)](https://travis-ci.org/sdl/embedded-rest-server) [![Maven  Central](https://img.shields.io/maven-central/v/com.fredhopper.server/embedded-rest-server.svg?style=flat-square)](http://search.maven.org/#browse%7C44355596)

# Embedded RESTful Server

This modules provides a minimal service provider interface (SPI) using [`ServiceLoader`][1] to launch an instance of embedded Jetty with potentially a number of [Jersey JAX-RS applications][2] and generic Jetty [context handlers][3].

## Service Interfaces

You can provide as many as implementations of the following interfaces in the class path to be loaded via Java `ServiceLoader`:

* [`LoggingConfigurator`][4] to be loaded in the initial `static` context to be able to apply any logging configuration early when JVM starts.
* [`ApplicationFactory`][5] to create an instance Jersey JAX-RS application.
* [`ContextHandlerFactory`][6] to create an instance of `ContextHandler` for Jetty.
* [`RequestLogHandlerFactory`][7] to create an instance `RequestLogHandler` for Jetty (e.g. to configure access logs in Jetty).

## Provided Implementations

By default, the following implementations are activated provided with the conditions:

* A context handler to serve at `/docs` if the environment values either contain existing `${application.name}.root/docs` or `${application.name}.docs.root`.

## Usage

The module provides a standard Java main class that can be used as:

```bash
$ java OPTIONS com.fredhopper.server.Bootstrap
```

or alternatively, if `embedded-rest-server.jar` is used:

```bash
$ java -jar embedded-rest-server.jar
```

or alternatively, use [`EmbeddedServer`][es] API in your code.

**Note** that when all the Maven dependencies, e.g. Jetty and Jersey, *must* be explicitly be declared
in your POM files. This module only exposes them in `provided` scope to allow downstream dependencies
to use their own versions.

## Contributing

Please refer to [here](sdl/oss-parent/CONTRIBUTING.md).

## License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[Complete License][license]

[1]: http://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html
[2]: https://jersey.java.net/
[3]: http://download.eclipse.org/jetty/stable-9/apidocs/org/eclipse/jetty/server/handler/ContextHandler.html
[4]: src/main/java/com/fredhopper/server/spi/LoggingConfigurator.java
[5]: src/main/java/com/fredhopper/server/spi/ApplicationFactory.java
[6]: src/main/java/com/fredhopper/server/spi/ContextHandlerFactory.java
[7]: src/main/java/com/fredhopper/server/spi/RequestLogHandlerFactory.java
[license]: LICENSE.txt
[es]: src/main/java/com/fredhopper/server/EmbeddedServer.java
