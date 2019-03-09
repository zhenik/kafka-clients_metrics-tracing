package io.zhenik.example.kafka.metrics;

import brave.Tracing;
import brave.kafka.streams.KafkaStreamsTracing;
import java.util.Optional;
import java.util.Properties;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;

public class StreamsProcessing implements Runnable {
  private final String topicIn;
  private final String topicOut;
  private final AppConfig config;
  private final KafkaStreams kafkaStreams;

  public StreamsProcessing(String topicIn, String topicOut, KafkaStreamsTracing kafkaStreamsTracing, AppConfig config) {
    this.topicIn = topicIn;
    this.topicOut = topicOut;
    this.config = config;
    this.kafkaStreams = kafkaStreamsTracing.kafkaStreams(buildTopology(), getDefaultProperties(config.getKafkaClientFactory().getBootstrapServers()));
  }

  Topology buildTopology() {
    StreamsBuilder builder = new StreamsBuilder();
    builder.stream(topicIn, Consumed.with(Serdes.String(), Serdes.String()))
        .mapValues(v -> v + "_added")
        .peek((k, v) -> System.out.println(k + " : "+v))
        .to(topicOut, Produced.with(Serdes.String(), Serdes.String()));
    return builder.build();
  }

  Properties getDefaultProperties(final String bootstrapServers) {
    final Properties properties = new Properties();
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-app-id");
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    return properties;
  }

  public KafkaStreams getKafkaStreams() { return kafkaStreams; }

  @Override
  public void run() { kafkaStreams.start(); }

  public void stopStreams() { Optional.ofNullable(kafkaStreams).ifPresent(KafkaStreams::close); }
}
