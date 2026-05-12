package com.sajithjeewantha.auth_service.controller;

import com.sajithjeewantha.auth_service.model.AuthResponse;
import com.sajithjeewantha.auth_service.model.LoginRequest;
import com.sajithjeewantha.auth_service.model.RegisterRequest;
import com.sajithjeewantha.auth_service.service.user.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/health")
    public String health() {
        return "Application running";
    }

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authService.authenticate(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
    }

    @GetMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> check(){
        return new ResponseEntity<>("Checking OK", HttpStatus.OK);
    }

    @GetMapping("/role")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> role(){
        return new ResponseEntity<>("Role OK", HttpStatus.OK);
    }
}
