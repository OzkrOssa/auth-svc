package co.com.crediya.usecase.login.exception;

public class LoginAttemptException extends RuntimeException{
    public LoginAttemptException(String message) {
        super(message);
    }
}
