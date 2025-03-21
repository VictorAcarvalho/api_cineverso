package com.cineverso.api_cineverso.controllers;


import com.cineverso.api_cineverso.models.StepsLabels;
import com.cineverso.api_cineverso.models.User.User;
import com.cineverso.api_cineverso.models.User.dto.CreateUserRequest;
import com.cineverso.api_cineverso.models.User.dto.UpdateUserRequest;
import com.cineverso.api_cineverso.services.UserService;
import com.cineverso.api_cineverso.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController()
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
    }

    @PostMapping( )
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> createUser(@RequestBody() CreateUserRequest request){
        log.info("[{}]- creating user for email: {}", StepsLabels.CREATE_USER, request.email());

        String savedCreateUser = userService.createUser(request);

        return ResponseEntity.status(201).body(savedCreateUser);
    }

    @PatchMapping("/delete")
    public ResponseEntity<String> deleteUser(){
        return  ResponseEntity.ok(userService.deleteAUserById());
    }

    @PatchMapping("/active")
    public ResponseEntity<String> activeUser(){
        return ResponseEntity.ok(userService.activeAUser());
    }

    @PatchMapping("/update")
    public  ResponseEntity<User> updateUser(@RequestBody UpdateUserRequest updateUserRequest){
        return ResponseEntity.ok(userService.updateUser(updateUserRequest));
    }

    @GetMapping()
    public ResponseEntity<User> getUserInfo(){
        return  ResponseEntity.ok(userService.getUserByToken());
    }

    @PostMapping("/admin" )
    public ResponseEntity<String> createAdminUser(@RequestBody() CreateUserRequest request){
        log.info("[{}]- creating user for email: {}", StepsLabels.CREATE_USER, request.email());

        String savedCreateUser = userService.createUser(request);

        return ResponseEntity.status(201).body(savedCreateUser);
    }

}

