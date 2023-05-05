package com.projetjavaopc.api.tools.userService;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AccountValidationService {
    private final JdbcTemplate jdbcTemplate;

    public AccountValidationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createAccountValidationToken(Integer userId, String token) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusHours(72); // le token expirera dans 24 heures
        jdbcTemplate.update("INSERT INTO account_validation (user_id, token, created_at, expires_at) VALUES (?, ?, ?, ?)",
                userId, token, now, expiresAt);
    }
}
