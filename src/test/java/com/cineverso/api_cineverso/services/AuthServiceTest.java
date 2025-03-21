package com.cineverso.api_cineverso.services;

import com.cineverso.api_cineverso.exceptions.BadRequestException;
import com.cineverso.api_cineverso.models.Auth.LoginRequest;
import com.cineverso.api_cineverso.models.Auth.LoginResponse;
import com.cineverso.api_cineverso.models.User.Roles;
import com.cineverso.api_cineverso.models.User.User;
import com.cineverso.api_cineverso.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("test@example.com")
                .password("$2a$10$W1Uq6UuOsXoE12.o5Np5t5TgdVZb7unlmGqG6V6gUM5KUbWhVuVY6")
                .active(true)
                .role(Roles.USER)
                .build();
    }

    @Test
    void testSuccessfulLogin() {
        when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));


        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);


        when(jwtService.generateToken(user)).thenReturn(new LoginResponse("jwt-token", 3600L));


        LoginRequest loginRequest = new LoginRequest(user.getEmail(), "password123");


        LoginResponse loginResponse = authService.authorization(loginRequest);


        assertNotNull(loginResponse);
        assertEquals("jwt-token", loginResponse.accesstoken());
        assertEquals(3600L, loginResponse.expiresIn());
    }

    @Test
    void testFailedLoginDueToInvalidPassword() {

        when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrongpassword", user.getPassword())).thenReturn(false);


        LoginRequest loginRequest = new LoginRequest(user.getEmail(), "wrongpassword");


        BadRequestException exception = assertThrows(BadRequestException.class, () -> authService.authorization(loginRequest));
        assertEquals("email or password is invalid", exception.getMessage());
    }

    @Test
    void testFailedLoginDueToNonExistingUser() {

        when(userService.findByEmail(user.getEmail())).thenReturn(Optional.empty());


        LoginRequest loginRequest = new LoginRequest(user.getEmail(), "password123");


        BadRequestException exception = assertThrows(BadRequestException.class, () -> authService.authorization(loginRequest));
        assertEquals("email or password is invalid", exception.getMessage());
    }
}
