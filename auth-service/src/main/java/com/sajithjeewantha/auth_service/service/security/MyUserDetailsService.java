package com.sajithjeewantha.auth_service.service.security;

import com.sajithjeewantha.auth_service.model.UserPrincipal;
import com.sajithjeewantha.auth_service.repo.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return authRepository.findByEmailEquals(email)
                .map(UserPrincipal::new)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid credentials"));
    }
}
