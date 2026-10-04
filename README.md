Absolutely — here is the **complete README as one copy-paste block**.

```markdown
# Kafka Order Processing POC

A small event-driven order processing application built with Java and Spring Boot to demonstrate Kafka, Redis, PostgreSQL, Docker, and REST API integration.

## Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL 17
- Apache Kafka
- Redis
- Docker & Docker Compose
- Kafka UI
- RedisInsight
- Maven

## Architecture

```text
                ┌─────────────────────┐
                │     REST Client     │
                │      /api/orders    │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │    Order Service    │
                │    Spring Boot      │
                └──────┬───────┬──────┘
                       │       │
                  Save Order    │ Publish Event
                       │       │
                       ▼       ▼
                ┌──────────┐  ┌─────────────────┐
                │PostgreSQL│  │      Kafka      │
                │          │  │  order-events   │
                └──────────┘  └────────┬────────┘
                                       │
                         ┌─────────────┴─────────────┐
                         │                           │
                         ▼                           ▼
                ┌─────────────────┐       ┌──────────────────┐
                │ Inventory       │       │ Notification     │
                │ Consumer        │       │ Consumer         │
                │                 │       │                  │
                │ inventory-      │       │ notification-    │
                │ service         │       │ service          │
                └────────┬────────┘       └──────────────────┘
                         │
                         ▼
                    ┌─────────┐
                    │  Redis  │
                    └─────────┘
```

## How It Works

1. A client creates an order through the REST API.
2. The order is persisted in PostgreSQL.
3. An `OrderCreated` event is published to the Kafka `order-events` topic.
4. The Inventory Consumer receives the event and updates inventory in Redis.
5. The Notification Consumer independently receives the same event and simulates sending an order confirmation.
6. Kafka consumer groups allow the two consumers to process the same event independently.

## REST APIs

### Create Order

```http
POST /api/orders
```

Example:

```json
{
  "productId": "P100",
  "quantity": 2,
  "customerEmail": "test@gmail.com"
}
```

### Get Order

```http
GET /api/orders/{id}
```

### Get All Orders

```http
GET /api/orders
```

### Get Inventory

```http
GET /api/inventory/{productId}
```

## Example cURL

Create an order:

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d "{\"productId\":\"P100\",\"quantity\":2,\"customerEmail\":\"test@gmail.com\"}"
```

Get an order:

```bash
curl http://localhost:8080/api/orders/1
```

Get all orders:

```bash
curl http://localhost:8080/api/orders
```

Check inventory:

```bash
curl http://localhost:8080/api/inventory/P100
```

## Kafka

The application publishes `OrderCreatedEvent` messages to:

```text
order-events
```

Two independent consumer groups process the event:

```text
inventory-service
notification-service
```

This demonstrates Kafka's publish/subscribe model where multiple consumer groups can independently consume the same event.

## Redis

Redis is used to store inventory values.

Example:

```text
inventory:P100 -> 98
```

When an order for quantity `2` is created, the inventory consumer updates the value accordingly.

RedisInsight can be used to inspect the stored inventory data.

## Docker

The project uses Docker Compose to run the supporting infrastructure:

- PostgreSQL
- Kafka
- Kafka UI
- Redis
- RedisInsight

Start the infrastructure with:

```bash
docker compose up -d
```

Stop the infrastructure with:

```bash
docker compose down
```

## Error Handling

The application includes centralized exception handling for missing orders.

For example:

```http
GET /api/orders/999
```

returns:

```json
{
  "error": "Order not found: 999"
}
```

with HTTP `404 Not Found`.

## Project Structure

```text
kafka-order-poc/
├── docker/
│   ├── Dockerfile.kafka
│   ├── Dockerfile.kafka-ui
│   └── Dockerfile.postgres
├── src/
│   └── main/
│       ├── java/
│       │   └── com/poc/kafka_order_poc/
│       │       ├── config/
│       │       ├── controller/
│       │       ├── dto/
│       │       ├── event/
│       │       ├── exception/
│       │       ├── kafka/
│       │       ├── mapper/
│       │       ├── model/
│       │       ├── repository/
│       │       └── service/
│       └── resources/
│           └── application.properties
├── docker-compose.yml
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

## Future Improvements

Potential extensions for this POC:

- Kafka retry and Dead Letter Topic (DLT)
- Better inventory validation
- Kafka producer failure handling
- Integration tests
- Docker health checks
- API documentation with OpenAPI/Swagger

## Purpose

This project was built as a hands-on demonstration of:

- Event-driven architecture
- Kafka producers and consumers
- Kafka consumer groups
- Redis-based inventory storage
- PostgreSQL persistence
- REST API design
- Spring Boot
- Docker-based local infrastructure
```

After you paste it into `README.md`, **save it but don't make another change yet**. Tell me `done`.