package com.jobportal.backend.service;

import com.jobportal.backend.dto.AuthResponse;
import com.jobportal.backend.dto.LoginRequest;
import com.jobportal.backend.dto.RegisterRequest;
import com.jobportal.backend.entity.User;
import com.jobportal.backend.repository.UserRepository;
import com.jobportal.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public String register(RegisterRequest request){
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(
                        request.getPassword()
                ))
                .role(request.getRole())
                .build();

        userRepository.save(user);

        logger.info("User registered: {}", user.getEmail());

        return "User registered successfully";
    }

    public AuthResponse login(LoginRequest request){

        User user = userRepository.findByEmail(
                request.getEmail()
        ).orElseThrow();

        if(!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )){
          logger.warn(
                  "Invalid login attempt: {}",
                  request.getEmail()
          );

          throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());

        logger.info("User logged in: {}", user.getEmail());

        return new AuthResponse(token);
    }



}
