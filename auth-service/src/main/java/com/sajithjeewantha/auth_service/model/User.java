package com.sajithjeewantha.auth_service.model;


import com.sajithjeewantha.shared_models.dto.user.Role;
import com.sajithjeewantha.shared_models.dto.user.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email", unique = true)
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Login username
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * User email
     */
    @Column(nullable = false, unique = true, length = 120)
    private String email;

    /**
     * BCrypt hashed password
     */
    @Column(nullable = false)
    private String password;

    /**
     * Account status
     */
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    /**
     * Roles assigned to user
     */
    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    /**
     * Account verification
     */
    @Builder.Default
    @Column(nullable = false)
    private boolean emailVerified = false;

    /**
     * Soft delete
     */
    @Column(nullable = false)
    private boolean deleted = false;

    /**
     * Security fields
     */
    @Column(nullable = false)
    private int failedLoginAttempts = 0;

    private LocalDateTime accountLockedUntil;

    /**
     * Audit fields
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime lastLoginAt;

    /**
     * Optimistic locking
     */
    @Version
    private Long version;


//    @PrePersist
//    public void prePersist() {
//        LocalDateTime now = LocalDateTime.now();
//        this.createdAt = now;
//        this.updatedAt = now;
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        this.updatedAt = LocalDateTime.now();
//    }

    public boolean isAccountLocked() {
        return accountLockedUntil != null &&
                accountLockedUntil.isAfter(LocalDateTime.now());
    }

    public void increaseFailedAttempts() {
        this.failedLoginAttempts++;
    }

    public void resetFailedAttempts() {
        this.failedLoginAttempts = 0;
        this.accountLockedUntil = null;
    }

    public void lockAccountUntil(LocalDateTime until) {
        this.accountLockedUntil = until;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }
}