FROM java:8
EXPOSE 5556
EXPOSE 5555
EXPOSE 8080
RUN mkdir /opt/jmx_prometheus_httpserver && wget 'http://central.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_httpserver/0.11.0/jmx_prometheus_httpserver-0.11.0-jar-with-dependencies.jar' -O /opt/jmx_prometheus_httpserver/jmx_prometheus_httpserver.jar
#COPY httpserver_sample_config.yml /opt/jmx_prometheus_httpserver/httpserver_sample_config.yml
CMD java -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=5555 -jar /opt/jmx_prometheus_httpserver/jmx_prometheus_httpserver.jar 8080 /opt/jmx_prometheus_httpserver/httpserver_sample_config.yml

#java -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=5555 -jar jmx_prometheus_httpserver-0.11.0-jar-with-dependencies.jar 8080 httpserver_sample_config.yml
