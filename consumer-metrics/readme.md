# [jms-exporter](https://github.com/prometheus/jmx_exporter)
## How 2 run
java -javaagent:./jms-exporter/jmx_prometheus_javaagent-0.11.0.jar=8080:./consumer-metrics/jmx-exporter-kafka-consumer-config.yaml -jar ./consumer-metrics/target/consumer-metrics-1.0-SNAPSHOT.jar