# Prometheus custom metrics & tracing with [Zipkin](https://zipkin.io/) 
`docker-compose up -d`

* Kafka-client metrics available on `localhost:8082`
* Custom metrics available on `localhost:9081/prometheus/metrics`
* Health-check available on `localhost:9081/healthcheck` 
* Tracing(Zipkin) UI available on `localhost:9411` 

## Custom metrics
Dependency
```xml
<dependency>
    <groupId>io.prometheus</groupId>
    <artifactId>simpleclient_dropwizard</artifactId>
    <version>0.6.0</version>
</dependency>
```

Code
```
  // Create registry for Dropwizard metrics.
  private static final MetricRegistry metrics = new MetricRegistry();
  // Create a Dropwizard counter.
  static final Counter counter = metrics.counter("my_custom_counter_total");
  
  ...
  counter.inc();
```

## Healthcheck 
Using Dropwizard all what is required is config file with adminConnectors.
```yaml
server:
  adminConnectors:
  - type: http
    port: ${ADMIN_PORT:-8081}
```  


`NB!`  

```
  // enable environment variables
  @Override
  public void initialize(Bootstrap<AppConfig> bootstrap) {
    bootstrap.setConfigurationSourceProvider(
        new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
            new EnvironmentVariableSubstitutor(false)));
    super.initialize(bootstrap);
  }
```

If env variable wil be provided it will override default value.
Example `$ADMIN_PORT`, default value `8081`

