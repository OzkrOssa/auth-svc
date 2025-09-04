package co.com.crediya.usecase.getrole;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.IRoleRepository;
import co.com.crediya.usecase.getrole.exception.InvalidRolException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetRoleUseCaseTest {
    @Mock
    private IRoleRepository repo;

    @InjectMocks
    private GetRoleUseCase getRoleUseCase;

    private Role role;

    @BeforeEach
    void setup(){
        role = Role.builder()
                .id(1L)
                .name("USER")
                .build();
    }

    @Test
    void test_GetRoleUseCase_Success() {
        when(repo.findById(any(Long.class))).thenReturn(Mono.just(role));

        StepVerifier.create(getRoleUseCase.execute(1L))
                .expectNext(role)
                .verifyComplete();
    }

    @Test
    void test_GetRoleUseCase_FailWithInvalidId() {
        when(repo.findById(any(Long.class))).thenReturn(Mono.error(new InvalidRolException("Invalid role ID")));

        StepVerifier.create(getRoleUseCase.execute(1L))
                .expectError(InvalidRolException.class)
                .verify();
    }
}
