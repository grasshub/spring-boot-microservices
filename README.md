# README #

This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###

* Quick summary
* Version
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

### How do I get set up? ###

* Summary of set up
* Configuration
* Dependencies
* Database configuration
* How to run tests
* Deployment instructions

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact


That being said, building an e-commerce microservices architecture with the technology stack you mentioned is quite 
involved. Below is a brief outline of how you might structure the services and implement the patterns you mentioned.

Project Structure
1. API Gateway

   Responsibility: Route requests to appropriate microservices.
   Technologies: Spring Cloud Gateway.

2. Eureka Server and Client

   Responsibility: Service registry and discovery.
   Technologies: Spring Eureka Server and Client

In Eureka registry, the service-id (Application) is case sensitive, and the service-id are all up cases even you 
specify the service-id as spring.application.name as all low cases.

netstat -ano | find "8761" (From cmd, find process running on port 8761 like 1234)
taskkill /PID 1234 /F (To kill this process)

3. Services

   Product Service: Manages product information.
   Order Service: Handles order creation, validation, and confirmation. Uses Axon Framework to implement Sagas for order
   management.
   Payment Service: Manages payment processing.
   Inventory Service: Handles inventory management.
   Notification Service: Manages the sending of notifications.
   Technologies: Spring Boot, Spring Cloud, Axon Framework.

4. Resilience Patterns

   Responsibility: Handle failures gracefully to maintain system stability.
   Patterns: Circuit Breaker and Bulkhead.
   Technologies: Resilience4j.

Steps to Implement
1. Setup Microservices

   Set up individual Spring Boot projects for each service.
   Define domain models and APIs for each service.
   Implement business logic for each service.

2. Setup Axon Framework

   Configure Axon in the Order Service to implement Sagas for managing orders.
   Use orchestration approach to coordinate the order services.

3. Setup Resilience4j

   Integrate Resilience4j in each service.
   Configure Circuit Breaker and Bulkhead patterns where appropriate.

4. Setup Consul and API Gateway

   Register each service with Consul for service discovery.

   When you are using Consul for service discovery and configuration, it is usually installed and run independently of 
   your Spring Boot applications. You generally do not run Consul as a part of a Spring Boot project. Instead, you 
   download and run Consul separately, and your Spring Boot applications, acting as Consul clients, will register with 
   it.

   Running Consul

   Download Consul: You can download Consul directly from the official website: 
   https://developer.hashicorp.com/consul/downloads. For Windows 11 64-bit, download the AMD64 version. Once downloaded,
   extract the zip file to C:\HashiCorp Consul directory.
   Run Consul: After downloading, you can start a Consul agent in development mode using the following ps command under
   C:\HashiCorp Consul directory as: ./consul agent -dev

   Verify Consul is Running

   Once Consul is running, open a web browser and navigate to the Consul UI at http://localhost:8500. You should see the
   Consul UI, and it should indicate that your Consul agent is running and healthy.

In production, you would typically run Consul in server mode and create a cluster of Consul servers for high availability.

For your Spring Boot projects, you would only need to include the Consul Discovery dependency and configure your 
application to communicate with the Consul agent.

   Configure API Gateway to route requests to the appropriate services using the information from Consul.

   Run & Test the Gateway

Once you have completed the above configuration, you can run the gateway application and test the routes by sending 
requests to the gateway's URL and observing if they are correctly routed to the respective services.

If you use Eureka Server and Client for service registry and discovery, and use Spring Cloud Gateway with enable 
discovery locator for dynamic routes for all microservices. You can access the product-service directly through:

URL: http://localhost:1111/api/product to display all products

Or through the gateway through dynamic route with service discover from

URL: http://localhost:8000/PRODUCT-SERVICE/api/product to display all products 

(http://gatewayhost:gatewayport/service-id/baseURI EUREKA registry is case sensitive and use all up cases service-id 
even you use low cases spring.application.name for configuration service-id in application.yml. You can verify from 
Eureka console at: http://localhost:8761)

5. Testing and Deployment

   Test each service individually and in combination with others.
   Deploy the microservices to a suitable environment.

Sample Pseudo Configuration

    Axon Configuration in Order Service

    java

@Configuration
public class AxonConfig {
// Configure Axon components like CommandBus, EventBus, Sagas, etc.
}

Resilience4j Configuration

java

@Configuration
public class ResilienceConfig {
// Configure Resilience4j components like CircuitBreaker, RateLimiter, Bulkhead, etc.
}

Consul Client Configuration

yaml

spring:
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        instanceId: ${spring.application.name}:${spring.application.instance_id:${random.value}}

API Gateway Configuration

yaml

    spring:
      cloud:
        gateway:
          routes:
            - id: productService
              uri: lb://product-service
              predicates:
                - Path=/product/**

Final Note

This is just a high-level overview and pseudo configuration, and implementing a full-fledged microservices-based 
e-commerce system involves thorough planning, designing, coding, and testing. You might need to dive deep into each 
component and technology to configure and integrate them properly based on your actual requirements.

To manually pass in logs and data directory containing db, add the mongod.cfg with content as follows:

systemLog:
  destination: file
  path: C:\mongodb-win32-x86_64-windows-7.0.0\logs\mongodb-log
  logAppend: true

storage:
  dbPath: C:\mongodb-win32-x86_64-windows-7.0.0\data

And launch mongo as: mongod.exe --config mongod.cfg

After running the Docker Desktop, eureka server will register microservice running at host.docker.internal:spring-cloud
-gateway.
The issue is altered hosts file by Docker Desktop under location: C:\Windows\System32\Drivers\etc\hosts, you will see
eureka server does a host lookup from IP and host.docker.internal is returned. (local machine from docker's perspective)
I guess Docker Desktop changed hosts with this line like follows:
# Added by Docker Desktop
192.168.1.190 host.docker.internal
(Where 192.168.1.190 is my HP Envy Desktop IP4 address from comcast)
In order to resolve this issue, just add one line above from my machine IP address to machine name from nslookup IP address
like:
192.168.1.190 HongEnvyDesktop.hsd1.de.comcast.net

Spring Data MongoDB - Relation Modelling

class Publisher {
    private String name;
    private String arconym;
    private int foundationYear;

    @ReadOnlyProperty
    @DocumentReference(lookup="{'publisher':?#{#self._id} }")
    List<Book> books;
}

class Book {
    private String isbn13;
    private String title;
    private int pages;

    @DocumentReference(lazy=true)
    private Publisher publisher;
}

Use the following command to start Keycloak for the Authentication and Authorization for Spring Cloud Gateway to protect 
the Microservices.

docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:22.0.4 start-dev

Then access the Keycloak through localhost:8080 at web browser.

After login with admin/admin, create a new realm named: spring-boot-microservices-realm and create it. Then create a client
under Clients tab from top left, enter Client ID as: spring-cloud-client, and at next page select: Client authentication
on, and select "Service accounts roles" only, click Save button. Click Credentials tab, at the "Client secret" field, 
click the "Regenerate" button and copy the client secret to enter at the postman at specific test case under the 
Authorization tab, 

How to use docker Zipkin for the distributed tracing in Spring Boot 3.1.2 application:

docker run -d -p 9411:9411 openzipkin/zipkin

How to start Kafka from confluent local in Windows 11

./confluent local kafka start

./confluent local kafka topic create quickstart

./confluent local kafka topic produce quickstart // Write message to a topic, Ctrl-C to exit

./confluent local kafka topic consume quickstart --from-beginning // Consume message from a topic, Ctrl-C to exit

./confluent local kafka stop 

Start the docker container for Kafka for spring-boot-microservices through docker-composer.yml file, under root directory

docker compose up -d

You can find spring-boot-microservices container inside Docker Desktop

You can use jib maven plugin to build the docker image without Dockerfile. Just add the following maven plugin dependency:

<build>
    <plugins>
        <plugin>
            <groupId>com.google.cloud.tools</groupId>
            <artifactId>jib-maven-plugin</artifactId>
            <version>3.3.2</version>
            <configuration>
                <from>
                    <image>eclipse-temurin:20-jre</image>
                </from>
                <to>
                    <image>registry.hub.docker.com/umyuh1/${project.artifactId}</image>
                </to>
                <container>
                    <ports>
                        <port>8082</port>
                    </ports>
                </container>
            </configuration>
        </plugin>
    </plugins>
</build>

Also need to add your Docker Hub credentials to maven m2 settings.xml as:

<servers>
    <server>
        <id>registry.hub.docker.com</id>
        <username><DockerHub Username></username>
        <password><DockerHub Password></password>
    </server>
</servers>

You can find under your project Plugins section, there is jib plugin with jib:build to build the docker image and publish
it to docker hub under your account repository. jib:dockerBuild just build the image without push it to docker hub.

You will use: mvn clean compile jib:build under project level to build and publish docker image to docker hub.

How to set up postgreSQL docker container?

docker pull postgres:16
docker volume create inventory-service-postgreSQL
docker run -d --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=hangzhoU@1 -v inventory-service-postgreSQL:/var/lib/postgresql/data 
postgres:16

Need use -e POSTGRES_PASSWORD=XXXX to set up super user postgres password first time, and create docker volume ecommerce-postgres-volume
and mount it to /var/lib/postgresql/data folder inside postgres container to persist data after restart.

Then you can stop the postgreSQL windows service, and launch the pgAdmin to add a connection to postgreSQL container as
localhost and create a devuser fore development.

You can add the follow instructions under docker compose file to execute the initial queries to create postgre devuser,
initial database like orders-service and change the ownership of orders-service database to devuser as:

volumes:
# Copy the sql script to create devuser and database
- ./sql/orders-service.sql:/docker-entrypoint-initdb.d/orders-service.sql

(Create a local sql folder contains initial sql script orders-service.sql and mount it to docker 
/docker-entrypoint-initdb.d/orders-service.sql to be executed at docker initial stage.)

From docker command: docker exec -it postgres psql -U postgres 
Enter interactive psql shell to run SQl commands and manage your postgreSQL database.

In order to make sure all the microservices and dependent services like keycloak, kafka broker, inventory-service-PostgreSQL
can communicate to each other as service at different containers inside docker, all the services should belong to the 
same network. If you use docker-compose.yml to define all the services, docker will create all these services at different
containers and create a default network like spring-boot-microservices_default (default to root project name) for all
these services. You can visualize them inside Docker Desktop containers view within the hierarchy of same network as:
spring-boot-microservices.

Use docker network ls to find all docker network
and docker network inspect spring-boot-microservices_default to view all the details of specific network.

If you only update one service and want to only update this service with out affecting our service:

docker compose up -d --no-deps orders-service