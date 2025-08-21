package com.product.app.controller;

import com.product.app.dto.AuthDTO.*;
import com.product.app.entity.UserAccount;
import com.product.app.service.JwtService;
import com.product.app.service.UserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger log = LogManager.getLogger(AuthController.class);
    private final UserService users;
    private final JwtService jwt;

    public AuthController(UserService users, JwtService jwt) {
        this.users = users;
        this.jwt = jwt;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        UserAccount u = users.register(req.username(), req.password());
        String token = jwt.createToken(u.getUsername(), u.getRole());
        log.info("Registered user: {}, hashedPassword: {}", u.getUsername(), u.getPasswordHash());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        var u = users.find(req.username()).orElseThrow(() -> new RuntimeException("Bad credentials"));
        if (!users.matches(req.password(), u.getPasswordHash())) throw new RuntimeException("Bad credentials");
        return ResponseEntity.ok(new AuthResponse(jwt.createToken(u.getUsername(), u.getRole())));
    }
}
