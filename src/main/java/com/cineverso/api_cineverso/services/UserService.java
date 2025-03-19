package com.cineverso.api_cineverso.services;
import com.cineverso.api_cineverso.adapters.UserAdapter;
import com.cineverso.api_cineverso.exceptions.BadRequestException;
import com.cineverso.api_cineverso.exceptions.DefaultException;
import com.cineverso.api_cineverso.models.StepsLabels;
import com.cineverso.api_cineverso.models.User.User;
import com.cineverso.api_cineverso.models.User.dto.UpdateUserRequest;
import com.cineverso.api_cineverso.utils.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.cineverso.api_cineverso.models.User.dto.CreateUserRequest;
import com.cineverso.api_cineverso.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    private  final UserAdapter userAdapter;

    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, UserAdapter userAdapter, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userAdapter = userAdapter;
        this.jwtUtil = jwtUtil;
    }

    private User getAUserById(UUID userId){
        return userRepository.findById(userId).orElseThrow(()->
                new EntityNotFoundException("User not found"));
    }

    public User getUserByToken(){
       var userId = jwtUtil.extractUserIdFromToken();
       return getAUserById(userId);
    }

    public Optional<User> findByEmail(String userEmail){
        var user = userRepository.findByEmail(userEmail);
        log.info("User email {} founded in repository: {}",userEmail, !user.isEmpty());
        return user;
    }
    public String createUser(CreateUserRequest createUserRequest) {
        boolean existsUserWithThisEmail = userRepository.existsByEmail(createUserRequest.email());

        if (existsUserWithThisEmail) {
            throw new BadRequestException("Email already been used");
        }

        var handledUser = userAdapter.handleUser(createUserRequest);

        try {
             userRepository.save(handledUser);
        } catch (Exception e) {
            log.error("[{}] - Error trying create a user: {}",StepsLabels.CREATE_USER ,e.getMessage());
            throw new DefaultException("Something was wrong, please try again later");
        }
        return "User been created";
    }



    @Transactional
    public String deleteAUserById(){
        var user = getUserByToken();

        if(user.isActive()){
            user.setActive(false);
            try {
                log.info("[{}]- deleting user: {}", StepsLabels.DELETE_USER, user.getId());
                userRepository.save(user);
            } catch (Exception e) {
                log.info("[{}]- A error ocurred on deleting user: {}", StepsLabels.DELETE_USER ,e.getCause());
                throw new RuntimeException("Something was wrong");
            }
        }
        return "user has been deleted";
    }

    @Transactional
    public String activeAUser(){
        var user = getUserByToken();


        if(!user.isActive()){
            user.setActive(true);
            try {
                log.info("[{}]- activating user: {}", StepsLabels.ACTIVE_USER, user.getId());
                userRepository.save(user);
            } catch (Exception e) {
                log.error("[{}] A error ocurred on activating user: {}", StepsLabels.ACTIVE_USER ,e.getMessage());
                throw new RuntimeException("Something was wrong");
            }
        }

        return "user has been activated";
    }

    public User updateUser(UpdateUserRequest updateUserRequest){
        var user = getUserByToken();

        user.setUpdatedAt(LocalDateTime.now());
        user.setName(updateUserRequest.name());
        user.setPicture(updateUserRequest.picture());
        try {
           return userRepository.save(user);
        } catch (Exception e) {
            log.error("[{}] A error ocurred on updating user: {}", StepsLabels.UPDATE_USER ,e.getMessage());
            throw new RuntimeException("Something was wrong");
        }
    }

}
