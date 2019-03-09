FROM openjdk:8-jdk-stretch
COPY ./target/streams-metrics-1.0-SNAPSHOT.jar .
COPY ./config.yml .
# https://github.com/cstroe/java-jmx-in-docker-sample-app/blob/master/bin/entrypoint.sh
CMD java -Dcom.sun.management.jmxremote \
           -Dcom.sun.management.jmxremote=true \
           -Dcom.sun.management.jmxremote.local.only=false \
           -Dcom.sun.management.jmxremote.authenticate=false \
           -Dcom.sun.management.jmxremote.ssl=false \
           -Djava.rmi.server.hostname=$HOST \
           -Dcom.sun.management.jmxremote.host=$HOST \
           -Dcom.sun.management.jmxremote.port=$JMX_PORT \
           -Dcom.sun.management.jmxremote.rmi.port=$JMX_PORT \
           -jar streams-metrics-1.0-SNAPSHOT.jar server config.yml
