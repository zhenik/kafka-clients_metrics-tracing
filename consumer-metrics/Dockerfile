FROM java:8
COPY ./target/consumer-metrics-1.0-SNAPSHOT.jar .
EXPOSE 15555
EXPOSE 15556
CMD java -Dcom.sun.management.jmxremote \
           -Dcom.sun.management.jmxremote=true \
           -Dcom.sun.management.jmxremote.local.only=false \
           -Dcom.sun.management.jmxremote.authenticate=false \
           -Dcom.sun.management.jmxremote.ssl=false \
#           -Djava.rmi.server.hostname=127.0.0.1 \
           -Djava.rmi.server.hostname=${DOCKER_HOST_IP} \
           -Dcom.sun.management.jmxremote.port=15555 \
           -Dcom.sun.management.jmxremote.rmi.port=15556 \
           -jar consumer-metrics-1.0-SNAPSHOT.jar kafka:9092
