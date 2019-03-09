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

public class StreamsProcessing {
  private final String topicIn;
  private final String topicOut;

  public StreamsProcessing(String topicIn, String topicOut) {
    this.topicIn = topicIn;
    this.topicOut = topicOut;
  }

  Topology buildTopology() {
    StreamsBuilder builder = new StreamsBuilder();
    builder.stream(topicIn, Consumed.with(Serdes.String(), Serdes.String()))
        .mapValues(v -> v + "_added")
        .to(topicOut, Produced.with(Serdes.String(), Serdes.String()));
    return builder.build();
  }

  //public KafkaStreams getKafkaStreams() { return kafkaStreams; }

  //@Override
  //public void run() { kafkaStreams.start(); }
  //
  //public void stopStreams() { Optional.ofNullable(kafkaStreams).ifPresent(KafkaStreams::close); }
}
