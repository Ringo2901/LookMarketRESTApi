package by.bsuir.lookmanager.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private final String secret;

    public JwtProvider(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String createToken(Long userId) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .claim("userId", userId.toString())
                .signWith(key)
                .compact();
    }
}
