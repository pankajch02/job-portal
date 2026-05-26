package com.jobportal.backend.dto;

import com.jobportal.backend.entity.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
}
