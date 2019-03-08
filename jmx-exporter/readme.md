# Local run

## Coupled mode
`java -javaagent:./jmx_prometheus_javaagent-0.11.0.jar=8080:../consumer-metrics/jmx-exporter-kafka-consumer-config.yaml -jar ../consumer-metrics/target/consumer-metrics-1.0-SNAPSHOT.jar localhost:29092`
## Decoupled mode
### kafka-client(consumer)

```bash
java -Dcom.sun.management.jmxremote \
           -Dcom.sun.management.jmxremote=true \
           -Dcom.sun.management.jmxremote.local.only=false \
           -Dcom.sun.management.jmxremote.authenticate=false \
           -Dcom.sun.management.jmxremote.ssl=false \
           -Djava.rmi.server.hostname=127.0.0.1 \
           -Dcom.sun.management.jmxremote.port=5555 \
           -Dcom.sun.management.jmxremote.rmi.port=5556 \
           -jar consumer-metrics-1.0-SNAPSHOT.jar localhost:29092
```

### Exporter (can connect to docker container consumer)
Exporter
```bash
java -jar jmx_prometheus_httpserver-0.11.0-jar-with-dependencies.jar 8080 local_httpserver_sample_config.yml
```