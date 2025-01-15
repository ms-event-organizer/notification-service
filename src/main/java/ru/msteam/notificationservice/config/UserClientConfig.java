package ru.msteam.notificationservice.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.msteam.notificationservice.client.UserClientErrorDecoder;

@Configuration
public class UserClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new UserClientErrorDecoder();
    }
}
