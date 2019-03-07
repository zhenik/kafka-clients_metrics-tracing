package io.zhenik.example.kafka.metrics.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class ConsumerApplication {
  public static void main(String[] args) {
    final Properties properties = new Properties();
    //properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group-2");
    properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "consumer-id-2");
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

    consumer.subscribe(Collections.singletonList("topic-in"));
    try {
      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
        for (ConsumerRecord<String, String> record : records)
          System.out.println(record.offset() + " : " + record.value());
      }
    } finally {
      consumer.close();
    }

  }
}
