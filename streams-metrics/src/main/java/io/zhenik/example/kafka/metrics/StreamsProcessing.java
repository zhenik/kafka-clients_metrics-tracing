package io.zhenik.example.kafka.metrics;

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
  private final KafkaStreams kafkaStreams;

  public StreamsProcessing(String topicIn, String topicOut) {
    this.topicIn = topicIn;
    this.topicOut = topicOut;
    final Properties properties = new Properties();
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-app-id");
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    this.kafkaStreams = new KafkaStreams(buildTopology(new StreamsBuilder()), properties);
  }

  private Topology buildTopology(final StreamsBuilder builder) {
    builder.stream(topicIn, Consumed.with(Serdes.String(), Serdes.String()))
        .mapValues(v -> v + "_added")
        .to(topicOut, Produced.with(Serdes.String(), Serdes.String()));
    return builder.build();
  }

  @Override
  public void run() { kafkaStreams.start(); }

  public void stopStreams() { Optional.ofNullable(kafkaStreams).ifPresent(KafkaStreams::close); }
}
