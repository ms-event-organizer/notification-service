package ru.msteam.notificationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SimpleEmailNotificationSenderTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private SimpleEmailNotificationSender emailNotificationSender;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> mailMessageCaptor;

    @Value("${app.sender-email}")
    private String senderEmail;

    @Test
    void sendNotification_whenSuccess_shouldSendNotification() {
        String address = "rest@mail.com";
        String subject = "subject";
        String message = "message";

        emailNotificationSender.sendNotification(address, subject, message);

        verify(mailSender).send(mailMessageCaptor.capture());

        SimpleMailMessage mailMessage = mailMessageCaptor.getValue();

        assertThat(mailMessage.getTo(), is(new String[] {address}));
        assertThat(mailMessage.getSubject(), is(subject));
        assertThat(mailMessage.getText(), is(message));
        assertThat(mailMessage.getFrom(), is(senderEmail));
    }
}