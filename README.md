
Spring Boot Hazelcast Clustering demo 

This project shows how you can setup a clustered Vert.x embedded in Spring Boot, having the Spring container managing Hazelcast.which originate from 
https://github.com/vert-x3/vertx-examples/tree/master/spring-examples/springboot-clustering ，but the example is runing on
one machine ,i have do some change to made it adapt to really cluster machine.

It consists of three verticles.two on first machine,one on other machine.
The first one starts an HTTP server, expecting a `name` query parameter, and sending it on the event bus.
The second one listens to names sent on the event bus, and replies with a formatted message.
The third one listens to names sent on the event bus, and replies with a formatted message.
Eventually the message goes to the client in the HTTP response body.

== The code

In the `io.vertx.examples.spring.clustering.hazelcast.VertxProducer` class, a clustered Vert.x instance is created.
It uses a Spring-managed `com.hazelcast.core.HazelcastInstance`.

There are multiple ways to configure it, but a simple one is to put a `hazelcast.xml` file at the root of the classpath.

The Hazelcast instance will be created *before* and destroyed *after* Vert.x.

IMPORTANT: the Hazelcast shutdown hook must be disabled.

To disable it, simply add this to the `hazelcast.xml` file:

[source,xml]
----
  <properties>
    <!-- ... other properties ... -->
    <property name="hazelcast.shutdownhook.enabled">false</property>
  </properties>
----
== The application.properties

The host should be set the machine address which runing server, that can be used when servers run different machine.

host=xxx.xxx.xxx.xxx


== Trying

To see clustering in action, you should start multiple instances of this application.
The HTTP server will be setup only if you provide an `httpPort` system property.

So you can start the first instance with an HTTP server on port 8081:

[source,shell]
----
mvn spring-boot:run -DhttpPort=8081
----

Then start the other instances without the HTTP server:

[source,shell]
----
mvn spring-boot:run
----

And then start the other instances the other without the HTTP server:

[source,shell]
----
mvn spring-boot:run
----

It's time for testing now:

[source,shell]
----
seq 10 | while read i; do curl http://localhost:8081/?name=Thomas${i}; echo; done
----
