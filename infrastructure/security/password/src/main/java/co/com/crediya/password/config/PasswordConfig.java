package co.com.crediya.password.config;

import co.com.crediya.model.passwordencoder.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            private final org.springframework.security.crypto.password.PasswordEncoder delegate =
                    new BCryptPasswordEncoder();

            @Override
            public String encode(String rawPassword) {
                return delegate.encode(rawPassword);
            }

            @Override
            public boolean matches(String rawPassword, String encodedPassword) {
                return delegate.matches(rawPassword, encodedPassword);
            }
        };
    }
}
