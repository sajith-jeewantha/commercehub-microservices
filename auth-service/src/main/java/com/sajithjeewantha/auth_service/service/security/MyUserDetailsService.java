package com.sajithjeewantha.auth_service.service.security;

import com.sajithjeewantha.auth_service.cache.CacheNames;
import com.sajithjeewantha.auth_service.model.UserPrincipal;
import com.sajithjeewantha.auth_service.repo.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;

    @Override
    @Cacheable(value = CacheNames.USERS, key = "#email")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return authRepository.findByEmailWithRoles(email)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
    }
}
