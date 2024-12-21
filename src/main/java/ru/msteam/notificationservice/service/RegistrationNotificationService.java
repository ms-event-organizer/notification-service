package ru.msteam.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.msteam.notificationservice.client.UserClient;
import ru.msteam.notificationservice.dto.RegistrationNotification;
import ru.msteam.notificationservice.dto.UserDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationNotificationService {

    private final NotificationSender notificationSender;

    private final UserClient userClient;

    private final long NOTIFICATION_SERVICE_ID = 666;

    @KafkaListener(topics = "${app.kafka.registration-service.topic}")
    public void listen(RegistrationNotification registrationNotification) {
        log.info("Received notification from registration service: '{}'", registrationNotification);
        final UserDto eventOwner = userClient.getUser(NOTIFICATION_SERVICE_ID, registrationNotification.eventOwnerId());
        sendEventRegistrationNotification(eventOwner, registrationNotification);
    }

    private void sendEventRegistrationNotification(UserDto user, RegistrationNotification registrationNotification) {
        final String subject = "Received new event application";
        final String message = String.format("""
                Hi, %s!
                                
                You just received new application for event '%s' from user with email '%s'.
                                
                """, user.name(), registrationNotification.eventName(), registrationNotification.participantEmail());
        notificationSender.sendNotification(user.email(), subject, message);
    }
}
