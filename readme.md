# Dependency-Injection Container
The software design project. Implementing self-made DI-container.

#### Implement a DI container that should provide\use:
* External metadata of container-managed Java classes (XML format)
  1. Classes for initializing fields
  2. Name (required if there are several different configurations for one basic interface)
  3. Object lifecycle type (prototype, singleton, thread)
  4. Initialization parameters (for set methods)
  5. Constructor Parameters
* Java API for interacting with a DI container (getting objects, monitoring)
  1. References to created classes are stored inside the container
* Support for standard java annotations: Inject, Provider, Named. The Inject should work through interfaces (not specific classes)
* In case of an error (exception, logical error), the backtrace for the service should be returned

