# Kafka Order Processing POC

A small event-driven order processing application built with Java and Spring Boot to demonstrate Kafka-based asynchronous processing, PostgreSQL persistence, Redis caching, and containerized local development.

> Current status: the project includes a working REST API with PostgreSQL persistence for orders, while the Kafka consumer and Redis-driven event-processing pieces are intended future enhancements. The original design documentation is retained below to reflect the intended architecture and roadmap.

## Tech Stack

- Java 21
- Spring Boot 4.1.1
- Maven
- PostgreSQL 17
- Apache Kafka 4.0.1
- Redis
- Docker & Docker Compose
- Kafka UI
- Spring Data JPA
- Spring Data Redis
- Spring for Apache Kafka

## Architecture

The planned order-processing flow is:

```text
                    ┌─────────────────┐
                    │   REST Client   │
                    │    /api/orders  │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │  Spring Boot   │
                    │  Order Service │
                    └───────┬─────────┘
                            │
                    ┌───────┴────────┐
                    │                │
                    ▼                ▼
             ┌────────────┐   ┌──────────────┐
             │ PostgreSQL │   │    Kafka     │
             │            │   │ order-events │
             └────────────┘   └──────┬───────┘
                                     │
                              ┌──────┴──────┐
                              │             │
                              ▼             ▼
                       ┌────────────┐ ┌──────────────┐
                       │ Inventory  │ │ Notification │
                       │  Consumer  │ │   Consumer   │
                       └─────┬──────┘ └──────────────┘
                             │
                             ▼
                          ┌───────┐
                          │ Redis │
                          └───────┘
```

## Current Infrastructure

Docker Compose currently provides:

- PostgreSQL 17
- Apache Kafka 4.0.1
- Kafka UI

Kafka runs in KRaft mode as a single broker/controller for local development.

Kafka UI is available at:

http://localhost:8081

Kafka broker:

localhost:9092

PostgreSQL:

localhost:5432

## Features

### Implemented
- Spring Boot REST API
- PostgreSQL persistence using Spring Data JPA
- Dockerized PostgreSQL
- Dockerized Apache Kafka
- Kafka UI for monitoring and topic management
- Kafka configured in KRaft mode
- Order creation API
- Get order by ID API

### Planned
- Event-driven order processing architecture
- Redis-based inventory caching
- Kafka consumers for inventory and notifications
- Enhanced order processing and retry/error handling

## API

### Create Order

POST /api/orders

Example request:

```json
{
  "productId": "P100",
  "quantity": 2,
  "customerEmail": "test@gmail.com"
}
```

Example cURL:

```bash
curl --location "http://localhost:8080/api/orders" \
--header "Content-Type: application/json" \
--data "{
    \"productId\": \"P100\",
    \"quantity\": 2,
    \"customerEmail\": \"test@gmail.com\"
}"
```

### Get Order

GET /api/orders/{id}

Example:

```bash
curl --location "http://localhost:8080/api/orders/1"
```

## Running Locally

1. Start Docker infrastructure

```bash
docker compose up -d --build
```

Check running containers:

```bash
docker compose ps
```

2. Start Spring Boot

On Windows:

```bash
./mvnw.cmd spring-boot:run
```

The application runs on:

http://localhost:8080

3. Open Kafka UI

Open:

http://localhost:8081

The Kafka UI should show the local Kafka cluster.

## Docker Services

| Service | Port | Purpose |
| --- | --- | --- |
| PostgreSQL | 5432 | Order persistence |
| Kafka | 9092 | Event streaming |
| Kafka UI | 8081 | Kafka monitoring |
| Spring Boot | 8080 | REST API |

## Project Structure

```text
Kafka-POC/
├── docker/
│   ├── Dockerfile.postgres
│   ├── Dockerfile.kafka
│   └── Dockerfile.kafka-ui
├── src/
│   └── main/
│       ├── java/
│       └── resources/
├── docker-compose.yml
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

## Kafka Flow

The application will use Kafka to decouple order creation from downstream processing.

When an order is created:

```text
Client
  │
  ▼
POST /api/orders
  │
  ▼
Spring Boot
  │
  ├── Save Order → PostgreSQL
  │
  └── Publish OrderCreated Event
              │
              ▼
        Kafka: order-events
              │
        ┌─────┴─────┐
        │           │
        ▼           ▼
   Inventory    Notification
    Consumer      Consumer
        │
        ▼
      Redis
```

The inventory consumer will update cached inventory, while the notification consumer will simulate sending an order confirmation.

## Planned Kafka Components

- order-events Kafka topic
- OrderCreated event
- Kafka producer
- Inventory consumer
- Notification consumer
- Consumer groups
- Redis inventory cache
- Kafka retry/error handling

## Development Progress

- Spring Boot project setup
- Java 21 configuration
- Maven setup
- PostgreSQL integration
- Dockerized PostgreSQL
- Kafka Docker setup
- Kafka KRaft configuration
- Kafka UI
- Kafka UI ↔ Kafka connectivity
- Order entity
- Create Order API
- Get Order API
- PostgreSQL persistence
- Get All Orders API
- Kafka order-events topic
- Kafka producer
- OrderCreated event
- Inventory consumer
- Notification consumer
- Redis inventory cache
- Inventory API
- Kafka retry/error handling
- Final documentation and screenshots

## Purpose

This project is a focused learning and portfolio POC demonstrating how a Spring Boot application can combine:

- REST APIs for synchronous requests
- PostgreSQL for persistent data
- Apache Kafka for asynchronous event processing
- Redis for fast-access cached data
- Docker for reproducible local infrastructure

The goal is to demonstrate practical understanding of event-driven architecture and the integration of commonly used backend technologies in a small, reproducible project.

## Important Note for Current Repository State

The repository is currently in its foundational stage:

- The REST API and PostgreSQL persistence are working.
- Dockerized Kafka and Kafka UI are available for local experimentation.
- Event-driven consumers, Redis integration, and downstream processing are planned for future implementation.

This means the architecture section above still reflects the intended long-term system design, while the actual working code in the repository focuses on the order creation and lookup flow.