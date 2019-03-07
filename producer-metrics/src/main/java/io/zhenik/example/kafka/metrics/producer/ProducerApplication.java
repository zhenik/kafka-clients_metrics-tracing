package io.zhenik.example.kafka.metrics.producer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerApplication {
  public static void main(String[] args) throws InterruptedException, ExecutionException {
    final Properties properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
    properties.put(ProducerConfig.CLIENT_ID_CONFIG, "producer-id");
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    final String topic = "topic-in";

    final KafkaProducer<String, String> kafkaProducer =
        new KafkaProducer<String, String>(properties);

    for (int i = 0; i<10; i++) {
      final RecordMetadata recordMetadata =
          kafkaProducer.send(new ProducerRecord<String, String>(topic, i + "", "a" + i)).get();
      System.out.println(recordMetadata);
      Thread.sleep(1_000);
    }
  }
}
