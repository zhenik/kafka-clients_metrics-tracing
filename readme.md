# Kafka-clients jmx metrics
Different modes how to expose metrics from Kafka-clients + [jmx-exporter](https://github.com/prometheus/jmx_exporter).

### How to run
* Build project `./mvnw clean install`
* Run kafka `docker-compose up -d zookeeper kafka`
### Prometheus metrics
1. [With javaagent](./consumer-metrics/readme.md)
2. [Decoupled](./producer-metrics/readme.md)
### Metrics access
`http://localhost:9081/prometheus/metrics`

### References 
- [JMX from docker container](https://github.com/cstroe/java-jmx-in-docker-sample-app)
- Kafka related [Monitoring docker container jmx](https://docs.confluent.io/current/installation/docker/docs/operations/monitoring.html)
- [Kafka-clients metrics](https://docs.confluent.io/current/kafka/monitoring.html) to build patterns for jmx exporter agent
