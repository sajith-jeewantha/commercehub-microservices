package com.sajithjeewantha.auth_service.model;

public record AuthResponse(
        String accessToken,
        String tokenType,
        Long userId,
        String email
) {
}
