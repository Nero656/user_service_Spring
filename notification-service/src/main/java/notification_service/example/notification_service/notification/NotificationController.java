package notification_service.example.notification_service.notification;

import lombok.RequiredArgsConstructor;
import notification_service.example.notification_service.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotificationController {
    @Autowired
    private final EmailService emailService;

    @PostMapping("/message")
    public ResponseEntity<String> send(@RequestBody NotificationRequest request) {
        emailService.send(request.getEmail(), request.getText());
        return ResponseEntity.ok("Notification sent to " + request.getEmail());
    }

    @PostMapping("/create_user_no_kafka")
    public ResponseEntity<String> create(@RequestBody NotificationRequestUser request) {
        emailService.sendWelcomeEmail(request.getEmail(), request.getUsername());
        return ResponseEntity.ok("Notification sent to " + request.getEmail());
    }

    @PostMapping("/delete_user_no_kafka")
    public ResponseEntity<String> delete(@RequestBody NotificationRequestUser request) {
        emailService.sendDeletedEmail(request.getEmail(), request.getUsername());
        return ResponseEntity.ok("Notification sent to " + request.getEmail());
    }
}