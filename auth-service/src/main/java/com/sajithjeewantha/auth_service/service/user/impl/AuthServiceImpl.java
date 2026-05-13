package com.sajithjeewantha.auth_service.service.user.impl;


import com.sajithjeewantha.auth_service.exception.EmailAlreadyExistsException;
import com.sajithjeewantha.auth_service.exception.InvalidCredentialsException;
import com.sajithjeewantha.auth_service.model.*;
import com.sajithjeewantha.auth_service.repo.AuthRepository;
import com.sajithjeewantha.auth_service.service.security.JwtService;
import com.sajithjeewantha.auth_service.service.user.AuthService;
import com.sajithjeewantha.shared_models.dto.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public void register(RegisterRequest request) {

        if (authRepository.existsByEmailEquals(request.email())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(Role.ROLE_CUSTOMER))
                .build();

        authRepository.save(user);
    }


    @Override
    @Transactional(readOnly = true)
    public AuthResponse authenticate(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        if (!authentication.isAuthenticated()) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtService.generateToken(userPrincipal);

        return new AuthResponse(
                token,
                "Bearer",
                userPrincipal.getName(),
                userPrincipal.getUsername()
        );
    }

}
