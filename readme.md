# Kafka-clients jmx metrics
Different modes how to expose metrics from Kafka-clients + [jmx-exporter](https://github.com/prometheus/jmx_exporter).

### How to run
* Build project `./mvnw clean install`
* Run kafka `docker-compose up -d zookeeper kafka`

### Proof of concept 
1. [Prometheus metrics with javaagent](./consumer-metrics/readme.md)
![javaagent](./docs/jmx-exporter-standalone.png)
2. [Prometheus metrics with decoupled http server](./producer-metrics/readme.md)
![decoupled](./docs/jmx-exporter-decoupled.png)
3. [Custom prometheus metrics & tracing with zipkin](./streams-metrics/readme.md)
![custom](./docs/jmx-exporter-custom-metrics.png)

### References 
- [JMX from docker container](https://github.com/cstroe/java-jmx-in-docker-sample-app)
- Kafka related [Monitoring docker container jmx](https://docs.confluent.io/current/installation/docker/docs/operations/monitoring.html)
- [Kafka-clients metrics](https://docs.confluent.io/current/kafka/monitoring.html) to build patterns for jmx exporter agent
