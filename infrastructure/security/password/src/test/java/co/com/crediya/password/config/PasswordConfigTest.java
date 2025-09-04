package co.com.crediya.password.config;

import co.com.crediya.model.passwordencoder.PasswordEncoder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordConfigTest {

    private final PasswordConfig passwordConfig = new PasswordConfig();
    private final PasswordEncoder passwordEncoder = passwordConfig.passwordEncoder();

    @Test
    void shouldEncodePassword() {
        String rawPassword = "mySecret123";
        String encoded = passwordEncoder.encode(rawPassword);

        assertThat(encoded).isNotBlank();
        assertThat(encoded).isNotEqualTo(rawPassword);
    }

    @Test
    void shouldMatchEncodedPassword() {
        String rawPassword = "mySecret123";
        String encoded = passwordEncoder.encode(rawPassword);

        assertThat(passwordEncoder.matches(rawPassword, encoded)).isTrue();
        assertThat(passwordEncoder.matches("wrongPassword", encoded)).isFalse();
    }
}
