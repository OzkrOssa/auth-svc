package co.com.crediya.jwt;

import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

class JwtProviderTest {

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider();

        String secret = "miClaveSuperSegura123!@#abcDEF456$%^789ExtraLargaConMuchosChars987654321"; //

        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        jwtProvider.setSecretKey(secretKey);
        jwtProvider.setExpiration(3600000L);
    }

    @Test
    void testGenerateAndValidateToken() {
        String email = "test@example.com";
        String role = "ADMIN";

        String token = jwtProvider.generateToken(email, role);

        assertNotNull(token);
        assertTrue(jwtProvider.validateToken(token));
    }

    @Test
    void testGetEmailFromToken() {
        String email = "user@example.com";
        String role = "USER";

        String token = jwtProvider.generateToken(email, role);

        assertEquals(email, jwtProvider.getEmailFromToken(token));
    }

    @Test
    void testGetRoleFromToken() {
        String email = "user@example.com";
        String role = "USER";

        String token = jwtProvider.generateToken(email, role);

        assertEquals(role, jwtProvider.getRoleFromToken(token));
    }

    @Test
    void testValidateToken_InvalidToken() {
        String invalidToken = "this.is.not.a.valid.token";
        assertFalse(jwtProvider.validateToken(invalidToken));
    }
}
