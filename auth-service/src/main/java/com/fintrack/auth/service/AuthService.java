package com.fintrack.auth.service;

import com.fintrack.auth.client.UserClient;
import com.fintrack.auth.dto.AuthResponse;
import com.fintrack.auth.dto.LoginRequest;
import com.fintrack.auth.dto.RegisterRequest;
import com.fintrack.auth.dto.UserCreateRequest;
import com.fintrack.auth.model.Role;
import com.fintrack.auth.model.User;
import com.fintrack.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserClient userClient;

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
            .username(request.username())
            .password(passwordEncoder.encode(request.password()))
            .email(request.email())
            .role(Role.USER)
            .build();
        userRepository.save(user);
        userClient.createUser(new UserCreateRequest(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole().name()
        ));
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() ->
                    new UsernameNotFoundException("Not found user with username: " + request.username())
                );
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
