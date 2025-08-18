package com.fintrack.user.service;

import com.fintrack.user.dto.UserCreateRequest;
import com.fintrack.user.model.Role;
import com.fintrack.user.model.User;
import com.fintrack.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void createUser(UserCreateRequest request) {
        User user = User.builder()
                .id(request.id())
                .username(request.username())
                .email(request.email())
                .role(Role.valueOf(request.role()))
                .build();
        userRepository.save(user);
    }
}
