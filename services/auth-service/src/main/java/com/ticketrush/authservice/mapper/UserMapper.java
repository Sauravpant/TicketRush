package com.ticketrush.authservice.mapper;

import com.ticketrush.authservice.entity.User;
import com.ticketrush.authservice.entity.Role;
import com.ticketrush.authservice.dto.response.UserResponse;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );
    }
}