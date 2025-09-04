package co.com.crediya.model.tokenprovider;

public interface TokenProvider {
    String generateToken(String subject, String roleName);
    String getEmailFromToken(String token);
    String getRoleFromToken(String token);
    boolean validateToken(String token);
}

