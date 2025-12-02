package notification_service.example.notification_service.services;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmailServiceTest {
    private final JavaMailSender mailSender = mock(JavaMailSender.class);
    private final EmailService emailService = new EmailService(mailSender);

    @Test
    void testSendWelcomeEmail() {
        String email = "test@example.com";
        String username = "Ivan";

        emailService.sendWelcomeEmail(email, username);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();
        assertEquals(email, sentMessage.getTo()[0]);
        assertEquals("Ваш аккаунт создан", sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains(username));
    }

    @Test
    void testSendDeletedEmail() {
        String email = "test@example.com";
        String username = "Ivan";

        emailService.sendDeletedEmail(email, username);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();
        assertEquals(email, sentMessage.getTo()[0]);
        assertEquals("Ваш аккаунт удалён", sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains(username));
    }

    @Test
    void testSendCustom() {
        String email = "test@example.com";
        String text = "Hello!";

        emailService.send(email, text);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();
        assertEquals(email, sentMessage.getTo()[0]);
        assertEquals(text, sentMessage.getText());
    }
}
