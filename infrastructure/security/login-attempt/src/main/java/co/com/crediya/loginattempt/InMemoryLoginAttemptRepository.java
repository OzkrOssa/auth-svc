package co.com.crediya.loginattempt;

import co.com.crediya.model.loginattempt.LoginAttemptRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "security.login")
public class InMemoryLoginAttemptRepository implements LoginAttemptRepository {

    private Integer maxAttempts;   // máximo de intentos antes de bloquear
    private Integer lockDuration;  // duración del bloqueo en segundos

    private final Map<String, Integer> attempts = new ConcurrentHashMap<>();
    private final Map<String, Instant> lockTimestamps = new ConcurrentHashMap<>();

    @Override
    public void loginSucceeded(String email) {
        attempts.remove(email);
        lockTimestamps.remove(email);
    }

    @Override
    public void loginFailed(String email) {
        int currentAttempts = attempts.getOrDefault(email, 0) + 1;
        attempts.put(email, currentAttempts);

        if (currentAttempts >= maxAttempts) {
            lockTimestamps.put(email, Instant.now());
        }
    }

    @Override
    public boolean isBlocked(String email) {
        if (!lockTimestamps.containsKey(email)) {
            return false;
        }

        Instant lockTime = lockTimestamps.get(email);
        Instant unlockTime = lockTime.plusSeconds(lockDuration);

        if (Instant.now().isBefore(unlockTime)) {
            return true;
        } else {
            attempts.remove(email);
            lockTimestamps.remove(email);
            return false;
        }
    }

}

