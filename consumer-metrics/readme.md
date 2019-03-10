# Prometheus metrics with javaagent
![standalone](../docs/jmx-exporter-standalone.png)
```
java -javaagent:./jmx-exporter/jmx_prometheus_javaagent-0.11.0.jar=8080:./consumer-metrics/jmx-exporter-kafka-consumer-config.yaml -jar ./consumer-metrics/target/consumer-metrics-1.0-SNAPSHOT.jar localhost:29092
```  

Metrics available on `localhost:8080`


