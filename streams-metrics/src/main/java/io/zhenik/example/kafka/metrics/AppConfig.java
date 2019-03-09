package io.zhenik.example.kafka.metrics;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class AppConfig extends Configuration {

  @Valid @JsonProperty("kafka") private KafkaClientFactory kafkaClientFactory;
  @Valid @JsonProperty("zipkin") private ZipkinClientFactory zipkinClientFactory;

  public KafkaClientFactory getKafkaClientFactory() { return kafkaClientFactory; }
  public ZipkinClientFactory getZipkinClientFactory() { return zipkinClientFactory; }

  public static class KafkaClientFactory<K, V> {
    @Valid @NotNull private String bootstrapServers;

    public KafkaClientFactory() { }
    public KafkaClientFactory(String bootstrapServers) { this.bootstrapServers = bootstrapServers; }

    public String getBootstrapServers() { return bootstrapServers; }
    public void setBootstrapServers(String bootstrapServers) { this.bootstrapServers = bootstrapServers; }
    @Override public String toString() {
      return "KafkaClientFactory{" +
          "bootstrapServers='" + bootstrapServers + '\'' +
          '}';
    }
  }

  public static class ZipkinClientFactory<K, V> {
    @Valid
    @NotNull
    private String url;

    public ZipkinClientFactory() { }

    public ZipkinClientFactory(String url) { this.url = url; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    @Override public String toString() {
      return "ZipkinClientFactory{" +
          "url='" + url + '\'' +
          '}';
    }
  }
}
