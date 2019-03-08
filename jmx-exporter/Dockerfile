FROM java:8
EXPOSE 8080
RUN mkdir /opt/jmx_prometheus_httpserver && wget 'http://central.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_httpserver/0.11.0/jmx_prometheus_httpserver-0.11.0-jar-with-dependencies.jar' -O /opt/jmx_prometheus_httpserver/jmx_prometheus_httpserver.jar
# Config file local_httpserver_sample_config.yml will be provided via volume in docker-compose file
CMD java -jar /opt/jmx_prometheus_httpserver/jmx_prometheus_httpserver.jar 8080 /opt/jmx_prometheus_httpserver/httpserver_sample_config.yml