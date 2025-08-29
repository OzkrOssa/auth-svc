package co.com.crediya.usecase.registeruser;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.IUserRepository;
import co.com.crediya.usecase.registeruser.exception.EmailAlreadyExistsException;
import co.com.crediya.usecase.registeruser.exception.InvalidBaseSalaryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {
    @Mock
    private IUserRepository repo;

    @InjectMocks
    private RegisterUserUseCase useCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("test@example.com")
                .baseSalary(1000L)
                .build();

    }
    @Test
    void registerUser_success() {

        Mockito.when(repo.existByEmail(Mockito.any(String.class)))
                .thenReturn(Mono.just(false));

        Mockito.when(repo.save(Mockito.any(User.class)))
                .thenReturn(Mono.just(user));


        StepVerifier.create(useCase.execute(user))
                .expectNextMatches(savedUser ->
                        savedUser.getEmail().equals("test@example.com") &&
                                savedUser.getFirstName().equals("John") &&
                                savedUser.getLastName().equals("Doe") &&
                                savedUser.getBaseSalary().equals(1000L)
                )
                .verifyComplete();
    }

    @Test
    void registerUser_withEmailAlreadyExists() {

        Mockito.when(repo.existByEmail(Mockito.any(String.class)))
                .thenReturn(Mono.just(true));

        StepVerifier.create(useCase.execute(user))
                .expectErrorMatches(throwable -> throwable instanceof EmailAlreadyExistsException &&
                        throwable.getMessage().equals("email already exists"))
                .verify();
    }

    @Test
    void registerUser_withInvalidBaseSalary() {

        user.setBaseSalary(-1L);

        Mockito.when(repo.existByEmail(Mockito.any(String.class)))
                .thenReturn(Mono.just(false));

        StepVerifier.create(useCase.execute(user))
                .expectErrorMatches(throwable -> throwable instanceof InvalidBaseSalaryException &&
                        throwable.getMessage().equals("base salary not valid, must be between 0 and 15,000,000"))
                .verify();
    }
}
