# web-store-app-backend

Backend for an e-commerce web application created as part of my bachelor's degree thesis.

It is built with Spring Boot and provides the server-side logic for a web store, including REST APIs, persistence, security, and Elasticsearch-based search.

## Features

- Spring Boot 3 backend written in Java 17
- REST API for the e-commerce application
- MariaDB for relational data storage
- Elasticsearch for search functionality
- Spring Security with JWT-based authentication
- Validation support for request payloads
- Docker Compose setup for local development

## Technology Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Data Elasticsearch
- Spring Security
- MariaDB
- Elasticsearch 8.5.3
- Kibana 8.5.3
- Maven

## Requirements

Before running the project, make sure you have:

- Java 17 or newer
- Maven 3.8+ or the included Maven Wrapper
- Docker and Docker Compose

## Project Configuration

The default application settings are stored in:

```text
src/main/resources/application.properties
```

Important defaults include:

- MariaDB connection: `jdbc:mariadb://localhost:3306/test`
- MariaDB username: `root`
- MariaDB password: `your_password`
- Elasticsearch is expected to be available locally
- Allowed CORS origins include:
  - `http://localhost:4200`
  - `https://web-store-app-frontend.vercel.app`

## Running the Project

### 1. Start the infrastructure

This repository includes a `docker-compose.yml` file that starts:

- 2 Elasticsearch nodes
- Kibana
- MariaDB

Run:

```bash
docker compose up -d
```

This will expose the following services locally:

- MariaDB: `localhost:3306`
- Elasticsearch: `localhost:9200`
- Kibana: `localhost:5601`

### 2. Make sure the database exists

The application is configured to use a MariaDB database named `test`.

If the database has not been created yet, create it in your MariaDB instance before starting the application.

### 3. Start the backend

Using the Maven Wrapper:

```bash
./mvnw spring-boot:run
```

Or with Maven installed locally:

```bash
mvn spring-boot:run
```

### 4. Build and run the JAR

To build the application:

```bash
./mvnw clean package
```

Then run the generated JAR:

```bash
java -jar target/web-store-app-backend-0.0.1-SNAPSHOT.jar
```

## Useful Notes

- The application uses `spring.jpa.hibernate.ddl-auto=none`, so it will not automatically create or update database tables.
- Elasticsearch security is disabled in the Docker setup to simplify local development.
- If you change the database password in Docker Compose, update `spring.datasource.password` in `application.properties` as well.
- If your frontend runs on a different address, update the allowed CORS origins accordingly.

## Repository Structure

- `src/main/java` - application source code
- `src/main/resources` - configuration files
- `docker-compose.yml` - local development infrastructure
- `pom.xml` - Maven build configuration

## About the Project

This backend was developed for my bachelor's degree thesis as the server-side component of an e-commerce system. It is intended to support the core functionality of the web store, including data management, authentication, and search.
