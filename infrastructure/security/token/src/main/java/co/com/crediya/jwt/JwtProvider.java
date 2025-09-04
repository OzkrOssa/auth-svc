package co.com.crediya.jwt;

import co.com.crediya.model.tokenprovider.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
@ConfigurationProperties(prefix = "token.jwt")
@Getter
@Setter
public class JwtProvider implements TokenProvider {

    private long expiration;
    private SecretKey secretKey;

    @Override
    public String generateToken(String email, String role) {
        Instant now = Instant.now();
        Instant expirationTime = now.plusMillis(expiration);

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expirationTime))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    @Override
    public String getRoleFromToken(String token) {
        return parseClaims(token).get("role", String.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}