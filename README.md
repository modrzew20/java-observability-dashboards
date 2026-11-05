# Java Observability Dashboards

A microservices demo project showcasing Java application observability with **Spring Boot**, **Apache Kafka**, **WebClient**, **Prometheus**, and **Grafana**. It demonstrates async communication via Kafka topics and sync HTTP calls via WebClient between microservices, with full metrics monitoring.

## Architecture

```
┌─────────────┐    WebClient      ┌──────────────────┐
│             │ ───────────────►  │                  │
│  Order      │                   │  Notification    │
│  Service    │ ◄── Kafka ─────── │  Service         │
│  :8080      │    events         │  :8081           │
└──────┬──────┘                   └──────────────────┘
       │
       │ publishes to "order-events" topic
       ▼
┌──────────────┐
│              │
│  Kafka       │
│  (Kraft)     │
│  :9092       │
│              │
└──────────────┘
```

### Services

| Service             | Port | Role                                         |
|---------------------|------|----------------------------------------------|
| **Order Service**   | 8080 | REST API, Kafka producer, WebClient client   |
| **Notification Svc**| 8081 | Kafka consumer, REST endpoint for WebClient  |
| **Kafka**           | 9092 | Message broker (Kraft mode, no Zookeeper)    |
| **Prometheus**      | 9090 | Metrics collection & storage                 |
| **Grafana**         | 3000 | Dashboards & visualization                   |

### Communication Flow

1. **Client** → sends `POST /api/orders` to **Order Service**
2. **Order Service** → publishes `OrderEvent` to Kafka topic `order-events`
3. **Order Service** → calls **Notification Service** via `WebClient` (HTTP POST)
4. **Notification Service** → consumes events from Kafka and processes notifications

## Prerequisites

- **Docker** and **Docker Compose** installed on your machine.

## Quick Start

Build and start all services with a single command:

```bash
docker compose up --build
```

This will launch five containers: Kafka, Order Service, Notification Service, Prometheus, and Grafana.

**Grafana login:** `admin` / `admin`

## API Endpoints

### Order Service (`:8080`)

| Method | Endpoint            | Description              |
|--------|---------------------|--------------------------|
| POST   | `/api/orders`       | Create a new order       |
| GET    | `/api/orders`       | List all orders          |
| GET    | `/api/orders/{id}`  | Get order by ID          |
| GET    | `/actuator/prometheus` | Prometheus metrics   |

### Notification Service (`:8081`)

| Method | Endpoint                    | Description                  |
|--------|-----------------------------|------------------------------|
| POST   | `/api/notifications/send`   | Receive notification (WebClient) |
| GET    | `/api/notifications`        | List all notifications       |
| GET    | `/api/notifications/count`  | Get notification count       |
| GET    | `/actuator/prometheus`      | Prometheus metrics           |

### Example: Create an Order

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerName": "John Doe", "product": "Laptop", "amount": 999.99}'
```

To stop and clean up:

```bash
docker compose down
```
