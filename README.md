# Sample Metrics Time Series using TimescaleDB

Demonstrate simple monitor for request counter and request latency average.
This project's using:
+ Spring Framework: quick boilerplate for simple REST API, Dependency Injection, Request Interceptor.
+ Adobe Metrics Client for Java: https://github.com/adobe/metrics-client-for-java
+ TimescaleDB for storing Time-series data.

## Instruction:

1. Start TimescaleDB with Docker: `docker/run.sh`
2. Run main class `App.java`
3. Open browser: http://localhost:8080/ping
4. See the result in any Postgresql Client or visualize it with Grafana. Cheer :)