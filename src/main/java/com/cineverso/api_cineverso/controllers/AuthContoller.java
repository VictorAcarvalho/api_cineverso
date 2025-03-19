package com.cineverso.api_cineverso.controllers;

import com.cineverso.api_cineverso.models.Auth.LoginRequest;
import com.cineverso.api_cineverso.models.Auth.LoginResponse;
import com.cineverso.api_cineverso.repositories.UserRepository;
import com.cineverso.api_cineverso.services.AuthService;
import com.cineverso.api_cineverso.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthContoller {

    private final AuthService authService;

    public AuthContoller(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        return  ResponseEntity.ok(authService.authorization(loginRequest));
    }
}
