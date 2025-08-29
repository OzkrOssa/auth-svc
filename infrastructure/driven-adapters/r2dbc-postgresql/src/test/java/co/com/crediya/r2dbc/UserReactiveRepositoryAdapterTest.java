package co.com.crediya.r2dbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {
    @Mock
    UserReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Mock
    private TransactionalOperator txOperator;

    @InjectMocks
    UserReactiveRepositoryAdapter adapter;



    private UserEntity userEntity;
    private User user;


    @BeforeEach
    void setup(){
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("firstName");
        userEntity.setLastName("lastName");
        userEntity.setEmail("email");
        userEntity.setPhone("phone");
        userEntity.setBaseSalary(100000L);

        user = new User();
        userEntity.setId(1L);
        userEntity.setFirstName("firstName");
        userEntity.setLastName("lastName");
        userEntity.setEmail("email");
        userEntity.setPhone("phone");
        userEntity.setBaseSalary(100000L);
    }

    @Test
    void mustExistByEmail(){
        when(repository.existsByEmail(userEntity.getEmail())).thenReturn(Mono.just(true));
        when(txOperator.transactional(any(Mono.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        Mono<Boolean> result = adapter.existByEmail(userEntity.getEmail());

        StepVerifier.create(result)
                .expectNextMatches(b -> b.equals(true))
                .verifyComplete();
    }

}
