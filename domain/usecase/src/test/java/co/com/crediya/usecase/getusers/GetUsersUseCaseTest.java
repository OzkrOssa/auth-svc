package co.com.crediya.usecase.getusers;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class GetUsersUseCaseTest {
    @Mock
    private IUserRepository repo;

    @InjectMocks
    private GetUsersUseCase useCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("test@example.com")
                .baseSalary(1000L)
                .build();

    }

    @Test
    void getUsers_success() {
        User otherUser = User.builder()
                .id(2L)
                .firstName("Lexi")
                .lastName("Doe")
                .email("test@example.com")
                .baseSalary(1000L)
                .build();

        Mockito.when(repo.findAll())
                .thenReturn(Flux.just(user, otherUser));

        StepVerifier.create(useCase.execute()).expectNextMatches(
                usr -> usr.getId().equals(1L) &&
                        usr.getFirstName().equals("John") &&
                        usr.getLastName().equals("Doe") &&
                        usr.getEmail().equals("test@example.com")
        ).expectNextMatches(
                ousr -> ousr.getId().equals(2L) &&
                        ousr.getFirstName().equals("Lexi") &&
                        ousr.getLastName().equals("Doe") &&
                        ousr.getEmail().equals("test@example.com")
        ).verifyComplete();
    }

    @Test
    void getUsers_empty() {
        Mockito.when(repo.findAll())
                .thenReturn(Flux.empty());

        StepVerifier.create(useCase.execute()).expectNextCount(0)
                .expectComplete();
    }
}
