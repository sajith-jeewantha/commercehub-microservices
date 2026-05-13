package com.sajithjeewantha.auth_service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sajithjeewantha.shared_models.dto.user.Role;
import com.sajithjeewantha.shared_models.dto.user.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@Getter
public class UserPrincipal implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final boolean accountLocked;
    private final UserStatus status;
    private final Set<String> roles;

    /**
     * Used at login time — User entity is session-bound here,
     * so getRoles() and all lazy fields are safely accessible.
     */
    public UserPrincipal(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.accountLocked = user.isAccountLocked();
        this.status = user.getStatus();
        this.roles = user.getRoles().stream()
                .map(Role::name)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * @JsonCreator — used by Jackson when deserializing from Redis.
     * All fields are primitives/enums/strings: no session needed, ever.
     */
    @JsonCreator
    public UserPrincipal(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("accountLocked") boolean accountLocked,
            @JsonProperty("status") UserStatus status,
            @JsonProperty("roles") Set<String> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.accountLocked = accountLocked;
        this.status = status;
        this.roles = roles != null ? roles : Set.of();
    }


    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserStatus.ACTIVE.equals(status);
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

}
