# web-store-app-backend

Backend for an e-commerce web application created as part of my bachelor's degree thesis.

This project provides the server-side foundation for a web store. It exposes REST APIs and handles the core backend responsibilities of the application, including business logic, persistence, authentication, and search.

## What this project does

The backend is responsible for:

- handling HTTP requests from the frontend
- storing and retrieving application data from MariaDB
- providing search capabilities through Elasticsearch
- securing endpoints with Spring Security and JWT authentication
- validating incoming data before processing it
- supporting the frontend of the e-commerce application

## Main features

- Spring Boot 3 backend written in Java 17
- REST API for the e-commerce application
- MariaDB for relational data storage
- Elasticsearch for search functionality
- Spring Security with JWT-based authentication
- Request validation support
- Docker Compose setup for local development

## Technology stack

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

## Configuration

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

If you run the frontend on another address, update the CORS configuration accordingly.

## How to run the project locally

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

If the database has not been created yet, create it in your MariaDB instance before starting the backend.

### 3. Run the backend from source

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

## Useful notes

- The application uses `spring.jpa.hibernate.ddl-auto=none`, so it will not automatically create or update database tables.
- Elasticsearch security is disabled in the Docker setup to simplify local development.
- If you change the database password in Docker Compose, update `spring.datasource.password` in `application.properties` as well.
- Make sure the database schema is prepared before running the application.

## Project structure

- `src/main/java` - application source code
- `src/main/resources` - configuration files
- `docker-compose.yml` - local development infrastructure
- `pom.xml` - Maven build configuration

## About the project

This backend was developed for my bachelor's degree thesis as the server-side component of an e-commerce system. It is intended to support the core functionality of the web store, including data persistence, authentication, product search, and communication with the frontend application.
