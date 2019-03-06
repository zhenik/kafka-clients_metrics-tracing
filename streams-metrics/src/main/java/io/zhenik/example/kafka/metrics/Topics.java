package io.zhenik.example.kafka.metrics;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.errors.TopicExistsException;

public class Topics {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    // Create topics
    Properties adminConfig = new Properties();
    adminConfig.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");

    //final AdminClient adminClient = AdminClient.create(adminConfig);
    Collection<NewTopic> newTopics =
        Arrays.asList(
            new NewTopic("topic-in", 1, (short) 1),
            new NewTopic("topic-out", 1, (short) 1));
    final AdminClient adminClient = KafkaAdminClient.create(adminConfig);

    adminClient.createTopics(newTopics).all().get();
  }

}
