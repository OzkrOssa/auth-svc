package co.com.crediya.loginattempt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;

class InMemoryLoginAttemptRepositoryTest {

    private InMemoryLoginAttemptRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLoginAttemptRepository();
        repository.setMaxAttempts(3);
        repository.setLockDuration(1);
    }

    @Test
    void whenLoginSucceeded_thenAttemptsAndLockAreCleared() {
        String email = "user@test.com";
        repository.getAttempts().put(email, 2);
        repository.getLockTimestamps().put(email, Instant.now());

        repository.loginSucceeded(email);

        assertThat(repository.getAttempts()).doesNotContainKey(email);
        assertThat(repository.getLockTimestamps()).doesNotContainKey(email);
    }

    @Test
    void whenLoginFailed_thenAttemptsIncrementedAndBlockedOnMaxAttempts() {
        String email = "user@test.com";

        repository.loginFailed(email);
        repository.loginFailed(email);
        assertThat(repository.isBlocked(email)).isFalse();
        assertThat(repository.getAttempts().get(email)).isEqualTo(2);

        repository.loginFailed(email);
        assertThat(repository.isBlocked(email)).isTrue();
    }

    @Test
    void whenBlocked_thenIsBlockedReturnsFalseAfterDurationExpires() {
        String email = "user@test.com";

        for (int i = 0; i < 3; i++) {
            repository.loginFailed(email);
        }
        assertThat(repository.isBlocked(email)).isTrue();

        // esperar más que lockDuration (1 segundo)
        await().atMost(2, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(repository.isBlocked(email)).isFalse());
    }

    @Test
    void whenNotBlocked_thenIsBlockedReturnsFalse() {
        String email = "user@test.com";

        assertThat(repository.isBlocked(email)).isFalse();
    }
}
