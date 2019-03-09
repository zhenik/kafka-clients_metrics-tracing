package io.zhenik.example.kafka.metrics.consumer;

import brave.Tracer;
import brave.Tracing;
import brave.kafka.clients.KafkaTracing;
import brave.sampler.Sampler;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import zipkin2.Span;
import zipkin2.codec.Encoding;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.urlconnection.URLConnectionSender;

public class ConsumerApplication {
  public static void main(String[] args) {
    if (args.length==0) System.exit(1);
    final String bootstrapServers = args[0];

    //Tracing
    URLConnectionSender sender = URLConnectionSender.newBuilder()
        .endpoint("http://zipkin:9411/api/v2/spans")
        .encoding(Encoding.PROTO3)
        .build();
    AsyncReporter<Span> reporter = AsyncReporter.builder(sender).build();
    Tracing tracing = Tracing.newBuilder()
        .localServiceName("kafka-consumer")
        .traceId128Bit(true)
        .sampler(Sampler.ALWAYS_SAMPLE)
        .spanReporter(reporter)
        .build();
    KafkaTracing kafkaTracing = KafkaTracing.newBuilder(tracing)
        .writeB3SingleFormat(true)
        .build();
    Tracer tracer = Tracing.currentTracer();

    final Properties properties = new Properties();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group-2");
    properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "consumer-id-2");
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    Consumer<String, String> consumer = kafkaTracing.consumer(new KafkaConsumer<>(properties));

    consumer.subscribe(Collections.singletonList("topic-in"));
    try {
      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
        for (ConsumerRecord<String, String> record : records) {
          brave.Span span = kafkaTracing.nextSpan(record).name("process").start();
          try (Tracer.SpanInScope sc = tracer.withSpanInScope(span)) {
            System.out.println(record.offset() + " : " + record.value());
          } catch (Exception e) {
            span.error(e);
            //log or throw
          } finally {
            span.finish();
          }

        }
      }
    } finally {
      consumer.close();
    }

  }
}
