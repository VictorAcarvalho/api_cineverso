package com.cineverso.api_cineverso.utils;

import com.cineverso.api_cineverso.models.Auth.LoginResponse;
import com.cineverso.api_cineverso.models.User.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class JwtUtil {

    private JwtEncoder encoder;

    private JwtDecoder decoder;

    public JwtUtil(JwtEncoder encoder, JwtDecoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public LoginResponse generateToken(User user){
        Instant now = Instant.now();
        long expiry = 3600L;



        var claims = JwtClaimsSet.builder()
                .issuer("spring-security-jwt")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(user.getId().toString())
                .claim("role",user.getRole())
                .build();

        return  new LoginResponse(  encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(), expiry);
    }

    public Jwt decodeToken(String token) {
        return decoder.decode(token);
    }

    public UUID extractUserIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = (Jwt) jwtAuthToken.getPrincipal();
            return extractUserIdFromJwt(jwt);
        }

        throw new RuntimeException("Usuário não autenticado com JWT");
    }
    private UUID extractUserIdFromJwt(Jwt jwt) {
        String subject = jwt.getSubject();

        if (subject != null && !subject.isEmpty()) {
            try {
                return UUID.fromString(subject);
            } catch (NumberFormatException e) {
                throw new RuntimeException("ID de usuário inválido no token", e);
            }
        }

        throw new RuntimeException("Não foi possível extrair o ID do usuário do token");
    }
}
