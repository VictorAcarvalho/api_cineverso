package com.cineverso.api_cineverso.models.User.dto;


import com.cineverso.api_cineverso.models.User.Roles;

public record CreateUserRequest(String name, String password, String email, Roles role) {
    public CreateUserRequest {

        if (role ==  null) {
            role = Roles.USER;
        }
    }
}
