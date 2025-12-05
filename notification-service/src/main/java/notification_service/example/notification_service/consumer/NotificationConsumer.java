package notification_service.example.notification_service.consumer;

import lombok.RequiredArgsConstructor;
import notification_service.example.notification_service.dto.UserCreatedEvent;
import notification_service.example.notification_service.dto.UserDeletedEvent;
import notification_service.example.notification_service.services.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = "user.created", groupId = "notification-service")
    public void consume(UserCreatedEvent event) {
        emailService.sendWelcomeEmail(event.getEmail(), event.getUsername());
    }

    @KafkaListener(topics = "user.deleted", groupId = "notification-service")
    public void consume(UserDeletedEvent event) {
        emailService.sendDeletedEmail(event.getEmail(), event.getUsername());
    }
}