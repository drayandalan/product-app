package com.product.app.service;

import com.product.app.entity.UserAccount;
import com.product.app.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository users;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository users) {
        this.users = users;
    }

    public UserAccount register(String username, String rawPassword) {
        UserAccount u = new UserAccount();
        u.setUsername(username);
        u.setPasswordHash(encoder.encode(rawPassword));
        return users.save(u);
    }

    public Optional<UserAccount> find(String username) {
        return users.findByUsername(username);
    }

    public boolean matches(String raw, String hash) {
        return encoder.matches(raw, hash);
    }
}
