# [jms-exporter](https://github.com/prometheus/jmx_exporter)
## How 2 run
```
java -javaagent:./jms-exporter/jmx_prometheus_javaagent-0.11.0.jar=8080:./consumer-metrics/jmx-exporter-kafka-consumer-config.yaml -jar ./consumer-metrics/target/consumer-metrics-1.0-SNAPSHOT.jar localhost:29092
```

java -javaagent:./jmx_prometheus_javaagent-0.11.0.jar=8080:../consumer-metrics/jmx-exporter-kafka-consumer-config.yaml -jar ../consumer-metrics/target/consumer-metrics-1.0-SNAPSHOT.jar localhost:29092

## Consumer metrics
```
kafka.consumer<type=consumer-fetch-manager-metrics, client-id=consumer-id-2, topic=topic-in><>bytes-consumed-total
kafka.consumer<type=consumer-node-metrics, client-id=consumer-id-2, node-id=node--1><>outgoing-byte-rate
kafka.consumer<type=consumer-fetch-manager-metrics, client-id=consumer-id-2, topic=topic-in><>records-consumed-total
kafka.consumer<type=consumer-fetch-manager-metrics, client-id=consumer-id-2, topic=topic-in, partition=0><>records-lead
kafka.consumer<type=consumer-metrics, client-id=consumer-id-2><>connection-count
kafka.consumer<type=consumer-coordinator-metrics, client-id=consumer-id-2><>commit-latency-max
kafka.consumer<type=consumer-metrics, client-id=consumer-id-2><>response-total
kafka.consumer<type=consumer-coordinator-metrics, client-id=consumer-id-2><>heartbeat-response-time-max
kafka.consumer<type=consumer-fetch-manager-metrics, client-id=consumer-id-2, topic=topic-in, partition=0><>records-lag
```

etc... 