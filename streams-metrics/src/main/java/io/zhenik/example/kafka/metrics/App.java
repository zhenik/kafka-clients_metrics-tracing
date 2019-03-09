package io.zhenik.example.kafka.metrics;

import brave.Tracing;
import brave.kafka.streams.KafkaStreamsTracing;
import brave.sampler.Sampler;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.MetricsServlet;
import io.prometheus.client.hotspot.DefaultExports;
import java.util.Properties;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zipkin2.Span;
import zipkin2.codec.Encoding;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.urlconnection.URLConnectionSender;

public class App extends Application<AppConfig> {
  private static Logger log = LoggerFactory.getLogger(App.class);

  // Create registry for Dropwizard metrics.
  private static final MetricRegistry metrics = new MetricRegistry();
  // Create a Dropwizard counter.
  static final Counter counter = metrics.counter("my_example_counter_total");

  public static void main(String[] args) throws Exception {
    new App().run(args);
  }

  // enable environment variables
  @Override
  public void initialize(Bootstrap<AppConfig> bootstrap) {
    bootstrap.setConfigurationSourceProvider(
        new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
            new EnvironmentVariableSubstitutor(false)));
    super.initialize(bootstrap);
  }

  @Override public void run(AppConfig appConfig, Environment environment) throws Exception {
    log.info("Configuration:\n {}", appConfig);

    //Tracing
    URLConnectionSender sender = URLConnectionSender.newBuilder()
        .endpoint("http://zipkin:9411/api/v2/spans")
        .encoding(Encoding.PROTO3)
        .build();
    AsyncReporter<Span> reporter = AsyncReporter.builder(sender).build();
    Tracing tracing = Tracing.newBuilder()
        .localServiceName("kafka-streams")
        .traceId128Bit(true)
        .sampler(Sampler.ALWAYS_SAMPLE)
        .spanReporter(reporter)
        .build();
    KafkaStreamsTracing kafkaStreamsTracing = KafkaStreamsTracing.create(tracing);

    StreamsProcessing streamsProcessing = new StreamsProcessing("topic-in", "topic-out");

    final Properties properties = new Properties();
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-app-id");
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    KafkaStreams kafkaStreams =
        kafkaStreamsTracing.kafkaStreams(streamsProcessing.buildTopology(), properties);
    kafkaStreams.start();

    log.info("Metrics initialization");
    counter.inc();
    CollectorRegistry.defaultRegistry.register(new DropwizardExports(metrics));
    // prometheus + dropwizard metrics are available on localhost:8081/prometheus/metrics
    environment.getAdminContext().addServlet(new ServletHolder(new MetricsServlet()), "/prometheus/metrics");
    // Add metrics about CPU, JVM memory etc.
    DefaultExports.initialize();
    log.info("Prometheus metrics are available on {}",  environment.getAdminContext().getContextPath() + "prometheus/metrics");

    final KafkaStreamsMetricsExports kafkaStreamsMetricsExports =
        new KafkaStreamsMetricsExports(kafkaStreams);
    kafkaStreamsMetricsExports.register();

    //final Map<MetricName, ? extends Metric> metrics = streamsProcessing.getKafkaStreams().metrics();
    //metrics.forEach(
    //    (k, v) -> {
    //      System.out.println(k.name());
    //      System.out.println(k.description());
    //      System.out.println(v.metricValue());
    //    });

  }


}
