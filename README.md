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