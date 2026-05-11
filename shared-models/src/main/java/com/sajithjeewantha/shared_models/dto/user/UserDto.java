package com.sajithjeewantha.shared_models.dto.user;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
    private Long id;
    private String name;
    private String email;
    private UserStatus status = UserStatus.ACTIVE;
    private Set<Role> roles = new HashSet<>();
    private boolean emailVerified = false;
    private boolean deleted = false;
    private int failedLoginAttempts = 0;
    private LocalDateTime accountLockedUntil;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
    private Long version;
}
