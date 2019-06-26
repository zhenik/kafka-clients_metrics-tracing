# Problem
How to write custom collector for KafkaJmx
Refs:
* https://github.com/prometheus/jmx_exporter/pull/305
 

## PoC
```
java -Dcom.sun.management.jmxremote=true \
           -Dcom.sun.management.jmxremote.local.only=false \
           -Dcom.sun.management.jmxremote.authenticate=false \
           -Dcom.sun.management.jmxremote.ssl=false \
           -Djava.rmi.server.hostname=localhost \
           -Dcom.sun.management.jmxremote.host=localhost \
           -Dcom.sun.management.jmxremote.port=6666 \
           -Dcom.sun.management.jmxremote.rmi.port=6666 \
           -jar example-jmx-1.0-SNAPSHOT.jar 
```

-Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=localhost -Dcom.sun.management.jmxremote.host=localhost -Dcom.sun.management.jmxremote.port=6666 -Dcom.sun.management.jmxremote.rmi.port=6666