package io.zhenik.example.kafka.metrics;

import io.prometheus.client.Collector;
import io.prometheus.client.CounterMetricFamily;
import io.prometheus.client.GaugeMetricFamily;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.streams.KafkaStreams;

public class KafkaStreamsMetricsExports extends Collector {
  private static final Logger LOGGER = Logger.getLogger(KafkaStreamsMetricsExports.class.getName());
  private final KafkaStreams kafkaStreams;

  //todo: reflect to bean (IoC)
  public KafkaStreamsMetricsExports(KafkaStreams kafkaStreams) {
    this.kafkaStreams = kafkaStreams;
  }


  @Override public List<MetricFamilySamples> collect() {

    final Map<MetricName, ? extends Metric> metrics = kafkaStreams.metrics();
    List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();

    final Optional<MetricName> requestTotal = metrics.keySet().stream().filter(k -> "request-total".equalsIgnoreCase(k.name())).findFirst();


    requestTotal.ifPresent(metricName -> mfs.add(

        new CounterMetricFamily("streams_request_total",
            "The total number of requests sent",
            Double.parseDouble(String.valueOf(metrics.get(metricName).metricValue()))
        )
    ));



    return mfs;
  }
}
