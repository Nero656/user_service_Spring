package notification_service.example.notification_service.services;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendWelcomeEmail(String email, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Ваш аккаунт создан");
        message.setText("Здравствуйте, " + username + "! Ваш аккаунт на сайте был успешно создан.");
        mailSender.send(message);
        System.out.println("Отправлено письмо на " + email);
    }

    public void sendDeletedEmail(String email, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Ваш аккаунт удалён");
        message.setText("Здравствуйте, " + username + "! Ваш аккаунт был удалён.");
        mailSender.send(message);
        System.out.println("Отправлено письмо об удалении на " + email);
    }

    public void send(String email, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setText(text);
        mailSender.send(message);
        System.out.println("Отправлено письмо на " + email);
    }
}
