package com.sajithjeewantha.auth_service.service.user;

import com.sajithjeewantha.auth_service.model.AuthResponse;
import com.sajithjeewantha.auth_service.model.LoginRequest;
import com.sajithjeewantha.auth_service.model.RegisterRequest;
import com.sajithjeewantha.auth_service.model.User;

import java.util.Optional;

public interface AuthService {
    void register(RegisterRequest request);
    AuthResponse authenticate(LoginRequest request);
}
