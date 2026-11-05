# Java Observability Dashboards

A demo project showcasing Java application observability with **Spring Boot Actuator**, **Prometheus**, and **Grafana**. It provides a ready-to-run environment for monitoring JVM metrics, HTTP request stats, and application health through pre-configured Grafana dashboards.

## Prerequisites

- **Docker** and **Docker Compose** installed on your machine.

## Quick Start

Build and start all services with a single command:

```bash
docker compose up --build
```

This will launch three containers:

| Service       | URL                      | Notes                          |
|---------------|--------------------------|--------------------------------|
| Demo App      | http://localhost:8080    | Spring Boot app with metrics   |
| Prometheus    | http://localhost:9090    | Metrics collection & storage   |
| Grafana       | http://localhost:3000    | Dashboards & visualization     |

**Grafana login:** `admin` / `admin`

Once running, the demo app exposes Prometheus metrics at `/actuator/prometheus`. Prometheus scrapes these metrics automatically, and Grafana visualizes them using the pre-provisioned dashboards.

To stop and clean up:

```bash
docker compose down
```
