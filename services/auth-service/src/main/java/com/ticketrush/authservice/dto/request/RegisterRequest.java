package com.ticketrush.authservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Please provide a valid email address")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, max = 64, message = "Password should be between 8 to 72 characters")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[@#*&%$!]).{8,}$",
                 message = "Password most contain one number and one special character")
        String password,

        @NotBlank(message = "Name cannot be empty")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String fullName,

        @Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
        String phoneNumber

) {
        
}
