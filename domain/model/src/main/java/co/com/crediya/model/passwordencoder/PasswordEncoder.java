package co.com.crediya.model.passwordencoder;

public interface PasswordEncoder {
    String encode(String password);
    boolean matches(String rawPassword, String encodedPassword);
}
