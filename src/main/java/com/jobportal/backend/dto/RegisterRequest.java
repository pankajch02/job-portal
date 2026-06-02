package com.jobportal.backend.dto;

import com.jobportal.backend.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(
            min = 6,
            message = "password must be at least 8 characters"
    )
    private String password;

    @NotNull(message = "Role is required")
    private Role role;
}
