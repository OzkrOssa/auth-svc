package co.com.crediya.usecase.getuser;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.IUserRepository;
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
class GetUserUseCaseTest {
    @Mock
    private IUserRepository repo;

    @InjectMocks
    private GetUserUseCase useCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("test@example.com")
                .baseSalary(1000L)
                .roleId(1L)
                .build();

    }

    @Test
    void getUser_success() {
        Mockito.when(repo.findByEmail(Mockito.anyString()))
                .thenReturn(Mono.just(user));

        StepVerifier.create(useCase.execute(user.getEmail()))
                .expectNextMatches(u ->
                        u.getId().equals(1L) &&
                                u.getFirstName().equals("John") &&
                                u.getLastName().equals("Doe") &&
                                u.getEmail().equals("test@example.com")
                )
                .verifyComplete();
    }

    @Test
    void getUser_userNotFound() {
        Mockito.when(repo.findByEmail(Mockito.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(user.getEmail()))
                .expectNextCount(0)
                .expectComplete();
    }
}
