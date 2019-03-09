package io.zhenik.example.kafka.metrics.producer;

import brave.Tracing;
import brave.kafka.clients.KafkaTracing;
import brave.sampler.Sampler;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import zipkin2.Span;
import zipkin2.codec.Encoding;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.urlconnection.URLConnectionSender;

public class ProducerApplication {
  public static void main(String[] args) throws InterruptedException, ExecutionException {
    if (args.length==0) System.exit(1);
    final String bootstrapServers = args[0];

    //Tracing
    URLConnectionSender sender = URLConnectionSender.newBuilder()
        .endpoint("http://zipkin:9411/api/v2/spans")
        .encoding(Encoding.PROTO3)
        .build();
    AsyncReporter<Span> reporter = AsyncReporter.builder(sender).build();
    Tracing tracing = Tracing.newBuilder()
        .localServiceName("kafka-producer")
        .sampler(Sampler.ALWAYS_SAMPLE)
        .traceId128Bit(true)
        .spanReporter(reporter)
        .build();
    KafkaTracing kafkaTracing = KafkaTracing.newBuilder(tracing)
        .writeB3SingleFormat(true)
        .build();

    System.out.println("Kafka bootstrapServer: "+bootstrapServers);
    final Properties properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    properties.put(ProducerConfig.CLIENT_ID_CONFIG, "producer-"+ UUID.randomUUID().toString());
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    final String topic = "topic-in";

    final Producer<String, String> kafkaProducer =
        kafkaTracing.producer(new KafkaProducer<>(properties));

    for (int i = 0; i<1000; i++) {
      final RecordMetadata recordMetadata =
          kafkaProducer.send(new ProducerRecord<>(topic, i + "", "a" + i)).get();
      System.out.println(recordMetadata);
      Thread.sleep(5_000);
    }
  }
}
