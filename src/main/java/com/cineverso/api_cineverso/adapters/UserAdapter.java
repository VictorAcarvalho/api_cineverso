package com.cineverso.api_cineverso.adapters;

import com.cineverso.api_cineverso.models.User.dto.CreateUserRequest;
import com.cineverso.api_cineverso.models.User.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component()
public class UserAdapter {

    private BCryptPasswordEncoder passwordEncoder;

    public UserAdapter(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User handleUser(CreateUserRequest createUserRecord){


        User handledUser = User.builder()
                .name(createUserRecord.name())
                .email(createUserRecord.email())
                .password(encodePassword(createUserRecord.password()))
                .role(createUserRecord.role())
                .build();

        return handledUser;
    }

    private String encodePassword(String password){
        return passwordEncoder.encode(password);
    }
}
