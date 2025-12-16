package notification_service.example.notification_service.listeners;

import lombok.RequiredArgsConstructor;
import notification_service.example.notification_service.dto.UserCreatedEvent;
import notification_service.example.notification_service.services.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCreateListener {
    private final EmailService emailService;

    @KafkaListener(
            topics = "user.created",
            groupId = "notification-service",
            containerFactory = "userCreatedKafkaListenerContainerFactory"
    )
    public void consume(UserCreatedEvent event) {
        emailService.sendWelcomeEmail(event.getEmail(), event.getUsername());
    }
}