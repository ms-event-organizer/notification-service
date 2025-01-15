package ru.msteam.notificationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.msteam.notificationservice.dto.UserDto;

@FeignClient(name = "userClient", url = "${app.user-service.url}", configuration = UserClientErrorDecoder.class)
public interface UserClient {

    @GetMapping("/{id}")
    UserDto getUser(@RequestHeader("X-User-Id") Long userId, @PathVariable Long id);
}
