package com.fintrack.auth.client;

import com.fintrack.auth.config.FeignS2SConfig;
import com.fintrack.auth.dto.UserCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "user-service",
    path = "/api/v1/users",
    configuration = FeignS2SConfig.class
)
public interface UserClient {

    @PostMapping
    void createUser(@RequestBody UserCreateRequest request);

}
