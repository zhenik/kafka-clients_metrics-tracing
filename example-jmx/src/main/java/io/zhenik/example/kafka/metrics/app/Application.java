package io.zhenik.example.kafka.metrics.app;

import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.metrics.KafkaMetric;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Instant;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class Application {

    public static void main(String[] args) throws Exception {

        String id = UUID.randomUUID().toString();
        final Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, id);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        properties.put(ProducerConfig.METRIC_REPORTER_CLASSES_CONFIG, StringSerializer.class);

        final String topic = "topic-in";

        final Producer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        Map<MetricName, ? extends Metric> metrics = kafkaProducer.metrics();
        for (Map.Entry<MetricName, ? extends Metric> entry : metrics.entrySet()) {
            System.out.println(entry.getKey());
            KafkaMetric value = (KafkaMetric) entry.getValue();
            System.out.println(value.metricValue());
            System.out.println();
        }

        System.out.println(metrics);
        DefaultExports.initialize();
        HTTPServer server = new HTTPServer(5555);
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));

        CustomProducerCollector customProducerCollector = new CustomProducerCollector(id);
        boolean b = customProducerCollector.validateMbeanObject("producer-metrics");
        Double mBeanAttributeValue = customProducerCollector.getMBeanAttributeValue("producer-metrics", "record-send-total", Double.class);
        System.out.println("HERE "+b);
        System.out.println(mBeanAttributeValue);
        customProducerCollector.register();


        while(true){
            Thread.sleep(1_000);
            long now = Instant.now().toEpochMilli();
            kafkaProducer.send(
                   new ProducerRecord<String, String>(topic, String.valueOf(now), now + " milliseconds"),
                    (metadata, exception)-> {
                       if (exception==null){
                           System.out.println("successfully sent");
                       } else {
                           System.out.println("fail sent");
                       }
                    }
            );
        }
    }
}
