package ru.msteam.notificationservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import ru.msteam.notificationservice.client.UserClient;
import ru.msteam.notificationservice.dto.RegistrationNotification;
import ru.msteam.notificationservice.dto.UserDto;
import ru.msteam.notificationservice.exception.NotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationNotificationServiceUnitTest {

    @Mock
    private NotificationSender notificationSender;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private RegistrationNotificationService registrationNotificationService;

    @Value("${app.notification-service-id}")
    private long notificationServiceId;

    @Test
    @DisplayName("Send notification when user found")
    void listen_whenUserFound() {
        RegistrationNotification notification = RegistrationNotification.builder()
                .eventOwnerId(12L)
                .eventName("event name")
                .participantEmail("participant@mail.com")
                .build();

        UserDto userDto = UserDto.builder()
                .id(12L)
                .name("owner name")
                .email("owner@mail.com")
                .build();

        when(userClient.getUser(notificationServiceId, eq(notification.eventOwnerId())))
                .thenReturn(userDto);

        registrationNotificationService.listen(notification);

        verify(userClient, times(1)).getUser(notificationServiceId, eq(notification.eventOwnerId()));
        verify(notificationSender, times(1)).sendNotification(eq(userDto.email()), anyString(), anyString());
    }

    @Test
    @DisplayName("Send notification when user not found")
    void listen_whenUserNotFound() {
        RegistrationNotification notification = RegistrationNotification.builder()
                .eventOwnerId(12L)
                .eventName("event name")
                .participantEmail("participant@mail.com")
                .build();

        when(userClient.getUser(anyLong(), eq(notification.eventOwnerId())))
                .thenThrow(new NotFoundException("User was not found"));

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> registrationNotificationService.listen(notification));

        assertThat(ex.getLocalizedMessage(), is("User was not found"));

        verify(userClient, times(1)).getUser(anyLong(), anyLong());
        verify(notificationSender, never()).sendNotification(anyString(), anyString(), anyString());
    }
}