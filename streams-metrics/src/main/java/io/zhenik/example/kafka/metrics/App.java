package io.zhenik.example.kafka.metrics;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.util.concurrent.ExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App extends Application<AppConfig> {
  private static Logger log = LoggerFactory.getLogger(App.class);

  public static void main(String[] args) throws Exception {
    new App().run(args);
  }

  // enable environment variables
  @Override
  public void initialize(Bootstrap<AppConfig> bootstrap) {
    bootstrap.setConfigurationSourceProvider(
        new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
            new EnvironmentVariableSubstitutor(false)));
    super.initialize(bootstrap);
  }

  @Override public void run(AppConfig appConfig, Environment environment) throws Exception {
    log.info("Configuration:\n {}", appConfig);

    StreamsProcessing streamsProcessing = new StreamsProcessing("topic-in", "topic-out");
    final ExecutorService executorService =
        environment.lifecycle()
            .executorService("kafka-streams-app-executor")
            .maxThreads(1)
            .build();
    executorService.submit(streamsProcessing);
  }

}
