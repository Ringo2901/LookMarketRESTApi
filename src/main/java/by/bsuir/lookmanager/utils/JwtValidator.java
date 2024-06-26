package by.bsuir.lookmanager.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtValidator {
    @Value("${jwt.secret}")
    private final String secret;

    public JwtValidator(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public Long validateTokenAndGetUserId(String token) throws JwtException {
        if (token == null){
            return 0L;
        }
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Jws<Claims> parsedToken = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
        Claims claims = parsedToken.getBody();
        return Long.parseLong(claims.get("userId", String.class));
    }
}
