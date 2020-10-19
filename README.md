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
5. Setup Grafana
+ Open Grafana local: http//localhost:3000/
+ User default: admin
+ Password default: admin
+ Go to Settings, select Data sources, add Data source, select PostgreSQL.
+ Edit config like photo below:
[![](https://i.ibb.co/7jnQ7g4/photo-2020-10-19-16-58-59.jpg")](https://i.ibb.co/7jnQ7g4)
+ Create Dashboard, and add new Query
+ Edit query like the photo below:
[![](https://i.ibb.co/QJtDvcS/photo-2020-10-19-17-07-13.jpg)](https://i.ibb.co/QJtDvcS/)
Enjoy!
