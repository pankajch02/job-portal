package com.jobportal.backend.dto;

import com.jobportal.backend.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Schema(
            example = "Pankaj Chahar"
    )
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Schema(
            example = "pankaj@gmail.com"
    )
    private String email;

    @Size(
            min = 6,
            message = "password must be at least 8 characters"
    )
    @Schema(
            example = "password123"
    )
    private String password;

    @NotNull(message = "Role is required")
    private Role role;
}
