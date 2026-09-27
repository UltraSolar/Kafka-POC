# Kafka Order Processing POC

A small event-driven order processing application built with Java and Spring Boot to demonstrate Kafka-based asynchronous processing, PostgreSQL persistence, Redis caching, and containerized local development.

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