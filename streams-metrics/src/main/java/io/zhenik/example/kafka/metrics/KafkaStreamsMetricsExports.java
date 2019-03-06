package io.zhenik.example.kafka.metrics;

import io.prometheus.client.Collector;
import java.util.ArrayList;
import java.util.List;

public class KafkaStreamsMetricsExports extends Collector {


  @Override public List<MetricFamilySamples> collect() {
    List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
    return null;
  }
}
