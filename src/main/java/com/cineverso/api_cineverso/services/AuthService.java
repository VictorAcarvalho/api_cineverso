package com.cineverso.api_cineverso.services;

import com.cineverso.api_cineverso.exceptions.BadRequestException;
import com.cineverso.api_cineverso.models.Auth.LoginRequest;
import com.cineverso.api_cineverso.models.Auth.LoginResponse;
import com.cineverso.api_cineverso.models.User.User;
import com.cineverso.api_cineverso.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AuthService {
    private  final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtUtil jwtService;

    public AuthService(BCryptPasswordEncoder passwordEncoder, UserService userService, JwtUtil jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public LoginResponse authorization(LoginRequest loginRequest){
        Optional<User> user = userService.findByEmail(loginRequest.email());


        if( user.isEmpty() || !passwordCheck(loginRequest.password(),user.get().getPassword())){

            throw new BadRequestException("email or password is invalid");
        }

        return jwtService.generateToken(user.get());
    }

    private boolean passwordCheck(String requestPassword, String savedPassword ){
        return  passwordEncoder.matches(requestPassword, savedPassword);
    }
}


