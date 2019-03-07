# todo

[Enable jmx for kafka-clients](https://docs.confluent.io/current/installation/docker/docs/operations/monitoring.html) 
try: `-Djava.rmi.server.hostname=127.0.0.1 -Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.authenticate=false  -Dcom.sun.management.jmxremote.ssl=false`

```
java -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=5555 -jar consumer-metrics-1.0-SNAPSHOT.jar
java -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -jar jmx_prometheus_httpserver-0.11.0-jar-with-dependencies.jar 8080 httpserver_sample_config.yml
```