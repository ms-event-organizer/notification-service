package ru.msteam.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimpleEmailNotificationSender implements NotificationSender {

    private final JavaMailSender emailSender;

    @Value("${app.sender-email}")
    private String senderEmail;

    @Override
    public void sendNotification(String toAddress, String subject, String message) {
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(toAddress);
        emailMessage.setSubject(subject);
        emailMessage.setText(message);
        emailMessage.setFrom(senderEmail);
        try {
            emailSender.send(emailMessage);
        } catch (MatchException ex) {
            log.error("Exception occurred while sending message to " + toAddress + ". /n" + ex.getLocalizedMessage());
        }
        log.debug("Notification to '{}' was send. Subject: '{}'", toAddress, subject);
    }
}
