package com.example.${moduleName};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * ${serviceName} Application Entry Point
 *
 * This is the main Spring Boot application class for the ${serviceName} microservice.
 * It serves as the entry point for starting the service and enabling various Spring features.
 *
 * Key Annotations:
 *   - @SpringBootApplication: Enables auto-configuration, component scanning,
 *                             and Spring Boot's default configuration
 *   - @EnableDiscoveryClient: Enables service registration and discovery with Nacos
 *
 * Usage:
 *   - Replace ${moduleName} with your module's package name (e.g., "user", "post", "court")
 *   - Replace ${serviceName} with your service name (e.g., "User Service", "Post Service")
 *   - Place this class in the root package of your module (e.g., com.example.user)
 *
 * Port Configuration:
 *   - The service runs on the port specified in application.yml (default: ${port})
 *
 * Database:
 *   - Connects to database: ${databaseName}
 *
 * To run:
 *   mvn spring-boot:run -pl service/${serviceName}
 *   or
 *   java -jar target/${serviceName}-1.0.0.jar
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ${serviceName}Application {

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(${serviceName}Application.class, args);
    }
}
