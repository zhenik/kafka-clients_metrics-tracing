# [jms-exporter](https://github.com/prometheus/jmx_exporter)
## How 2 run
```
- pattern :  kafka.consumer<type=(.+), client-id=(.+)><>(Count|Value)  
  name: kafka_consumer_$1_$2
  Labels:
    clientId: "$3"
```

java -javaagent:./jms-exporter/jmx_prometheus_javaagent-0.11.0.jar=8080:./producer-metrics/jmx-exporter-kafka-producer-config.yaml -jar ./producer-metrics/target/producer-metrics-1.0-SNAPSHOT.jar