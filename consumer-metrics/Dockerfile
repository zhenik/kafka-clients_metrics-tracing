FROM java:8
COPY ./target/consumer-metrics-1.0-SNAPSHOT.jar .
CMD java -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=5555 -jar consumer-metrics-1.0-SNAPSHOT.jar