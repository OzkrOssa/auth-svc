package co.com.crediya.usecase.login;

import co.com.crediya.model.loginattempt.LoginAttemptRepository;
import co.com.crediya.model.passwordencoder.PasswordEncoder;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.IRoleRepository;
import co.com.crediya.model.tokenprovider.TokenProvider;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.IUserRepository;
import co.com.crediya.usecase.login.exception.InvalidCredentialsException;
import co.com.crediya.usecase.login.exception.LoginAttemptException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {
    @Mock
    private IUserRepository userRepo;
    @Mock
    private IRoleRepository roleRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private LoginAttemptRepository loginAttemptRepository;

    @InjectMocks
    private LoginUseCase loginUseCase;

    private User user;
    private Role role;

    @BeforeEach
    void setup(){
        user = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("encodedPassword")
                .roleId(1L)
                .build();

        role = Role.builder()
                .id(user.getRoleId())
                .name("USER")
                .build();
    }

    @Test
    void test_LoginUseCase_Success() {
        when(loginAttemptRepository.isBlocked(anyString())).thenReturn(false);
        when(userRepo.findByEmail(anyString())).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        doNothing().when(loginAttemptRepository).loginSucceeded(anyString());
        when(roleRepo.findById(any(Long.class))).thenReturn(Mono.just(role));
        when(tokenProvider.generateToken(anyString(), anyString())).thenReturn("token");

        StepVerifier.create(loginUseCase.execute(user.getEmail(), "password"))
                .expectNext("token")
                .verifyComplete();
    }

    @Test
    void test_LoginUseCase_FailWithInvalidEmail() {
        when(userRepo.findByEmail(anyString())).thenReturn(Mono.error(new InvalidCredentialsException("Invalid email or password")));

        StepVerifier.create(loginUseCase.execute(user.getEmail(), "password"))
                .expectError()
                .verify();
    }

    @Test
    void test_LoginUseCase_FailWithInvalidPassword() {
        when(userRepo.findByEmail(anyString())).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(false);
        doNothing().when(loginAttemptRepository).loginFailed(anyString());
        StepVerifier.create(loginUseCase.execute(user.getEmail(), "password"))
                .expectError(InvalidCredentialsException.class)
                .verify();
    }

    @Test
    void test_LoginUseCase_FailBlockedEmail() {
        when(loginAttemptRepository.isBlocked(anyString())).thenReturn(true);
        StepVerifier.create(loginUseCase.execute(user.getEmail(), "password"))
                .expectError(LoginAttemptException.class)
                .verify();
    }
}
