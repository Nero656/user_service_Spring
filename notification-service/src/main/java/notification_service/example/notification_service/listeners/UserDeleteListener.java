package notification_service.example.notification_service.listeners;

import notification_service.example.notification_service.dto.UserDeletedEvent;
import notification_service.example.notification_service.services.EmailService;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDeleteListener {
    private final EmailService emailService;

    @KafkaListener(
            topics = "user.deleted",
            groupId = "notification-service",
            containerFactory = "userDeletedKafkaListenerContainerFactory"
    )
    public void consume(UserDeletedEvent event) {
        emailService.sendDeletedEmail(event.getEmail(), event.getUsername());
    }
}
