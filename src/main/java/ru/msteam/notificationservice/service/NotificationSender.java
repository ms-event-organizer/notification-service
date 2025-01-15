package ru.msteam.notificationservice.service;

public interface NotificationSender {

    void sendNotification(String toAddress, String subject, String message);
}
