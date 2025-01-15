package ru.msteam.notificationservice.dto;

import lombok.Builder;

@Builder
public record RegistrationNotification(
        Long eventOwnerId,

        String eventName,

        String participantEmail
) {
}
