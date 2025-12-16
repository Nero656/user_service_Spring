package notification_service.example.notification_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class kafkaTopicConfig {
    @Bean
    public NewTopic userCreatedTopic() {
        return new NewTopic("user.created", 1, (short) 1);
    }

    @Bean
    public NewTopic userDeletedTopic() {
        return new NewTopic("user.deleted", 1, (short) 1);
    }
}