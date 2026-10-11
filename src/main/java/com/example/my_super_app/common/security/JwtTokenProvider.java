package com.example.my_super_app.common.security;

import javax.crypto.SecretKey;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Nhà máy sản xuất & kiểm định token
 * Đây là class duy nhất “biết” cách tạo token và đọc token.
 * Mọi class khác không tự ký/giải mã JWT — chúng chỉ gọi qua JwtTokenProvider.
 * Đây là nguyên tắc single responsibility: logic JWT tập trung một chỗ, sau này đổi thuật toán ký (HS256 → RS256) chỉ sửa 1 class.
 */
@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration-ms}")
    private Long expirationMs;

    private SecretKey key;

    public SecretKey getKey() {
        if (key == null) {
            key = Keys.hmacShaKeyFor(secret.getBytes());
        }
        return key;
    }

    public String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("username", username)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getKey())
                .compact();

    }

    public Long getUserId(String token) {
        Claims claims = parseClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken (String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e)  {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
