# Kafka Order Processing POC

A single Spring Boot application demonstrating event-driven order processing with Kafka. Orders are stored in PostgreSQL; an `OrderCreated` event is consumed independently by inventory and notification listeners.

[Overview](#overview) · [API](#rest-api) · [Kafka reliability](#kafka-reliability) · [Run locally](#run-locally)

## Tech Stack

- Java 21 · Spring Boot 4.1.1 · Maven
- Spring Data JPA · Spring Kafka · MapStruct
- PostgreSQL 17 · Apache Kafka 4.0.1 · Redis 7
- Docker Compose · Kafka UI · RedisInsight

## Overview

This is **one Spring Boot application**, not a set of microservices. Its REST controllers and Kafka consumers run in the same application process.

```text
 REST client
  |
  v
  Order API -> Order service -> PostgreSQL
                    |
             OrderCreated producer
                    |
                    v
            Kafka: order-events
              /           \
             v             v
 Inventory listener   Notification listener
   (same app)             (same app)
       |                       |
       v                       v
     Redis              Notification log
  inventory:*          (simulated notification)
```

### Event flow

1. `POST /api/orders` validates and persists the order in PostgreSQL.
2. The application publishes an `OrderCreated` event to Kafka topic `order-events`.
3. The `inventory-service` consumer group updates Redis, using keys such as `inventory:P100`.
4. The independent `notification-service` consumer group logs a simulated notification; it does not send real email.

Each consumer group receives its own copy of the topic's events. Kafka's internal topics (for example, `__consumer_offsets`) are infrastructure topics, not application topics.

## REST APIs

All endpoints use the `/api` context path.

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/orders` | Create an order; returns `201 Created` |
| `GET` | `/api/orders/{id}` | Get an order by ID |
| `GET` | `/api/orders` | Get all orders |
| `GET` | `/api/inventory/{productId}` | Get inventory for a product |

### Create an order

```json
{
  "productId": "P100",
  "quantity": 2,
  "customerEmail": "test@gmail.com"
}
```

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d "{\"productId\":\"P100\",\"quantity\":2,\"customerEmail\":\"test@gmail.com\"}"
```

### Read orders and inventory

```bash
curl http://localhost:8080/api/orders/1
curl http://localhost:8080/api/orders
curl http://localhost:8080/api/inventory/P100
```

## Kafka

| Application topic | Consumer group | Behavior |
| --- | --- | --- |
| `order-events` | `inventory-service` | Updates inventory in Redis; configured with retry and DLT handling |
| `order-events` | `notification-service` | Simulates notification handling by logging a message |

### Kafka reliability

- **Inventory retry and DLT:** The inventory listener uses `@RetryableTopic` with `attempts = 4` and a 2-second backoff. After retries are exhausted, the message is routed to a Dead Letter Topic and handled by a custom `@DltHandler`.
- **Producer failure handling:** Producer retries are bounded (`retries = 2`) and `request.timeout.ms`, `delivery.timeout.ms`, and `max.block.ms` are configured. The send also has a 2-second wait limit, so an unavailable Kafka broker does not leave the API request waiting indefinitely.

## Redis

Inventory values use keys in the form `inventory:<productId>`, for example `inventory:P100`. For this POC, a missing product key is initialized with stock `100`, then reduced by the ordered quantity. RedisInsight is available for inspecting Redis data.

## Run locally

Start the supporting infrastructure:

```bash
docker compose up -d
```

Compose starts PostgreSQL, Kafka, Redis, Kafka UI, and RedisInsight. The application itself runs separately. Local UIs are available at Kafka UI `http://localhost:8081` and RedisInsight `http://localhost:5540`.

Stop the infrastructure when finished:

```bash
docker compose down
```

### Docker health checks

| Service | Readiness check |
| --- | --- |
| PostgreSQL | `pg_isready -U postgres -d orderdb` |
| Kafka | `/opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list` |
| Redis | `redis-cli ping` |

These checks distinguish a container that is merely running from a service that is ready to accept requests.

## Error Handling

Missing orders are handled centrally and return HTTP `404 Not Found`. For example, `GET /api/orders/999` returns:

```json
{
  "error": "Order not found: 999"
}
```

<details>
<summary>Project structure</summary>

```text
kafka-order-poc/
├── docker/
│   ├── Dockerfile.kafka
│   ├── Dockerfile.kafka-ui
│   ├── Dockerfile.mongo
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
</details>

## Future Improvements

Possible next steps include integration tests, stronger inventory validation, and API documentation with OpenAPI/Swagger.

## Purpose

This project is a hands-on demonstration of event-driven architecture, Kafka producers and consumer groups, Redis-backed inventory, PostgreSQL persistence, REST APIs, and Docker-based local infrastructure.