package co.com.crediya.model.loginattempt;

public interface LoginAttemptRepository {
    void loginSucceeded(String email);
    void loginFailed(String email);
    boolean isBlocked(String email);
}
