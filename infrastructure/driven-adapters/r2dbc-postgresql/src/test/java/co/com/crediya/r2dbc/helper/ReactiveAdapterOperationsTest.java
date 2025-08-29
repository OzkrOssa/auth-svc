package co.com.crediya.r2dbc.helper;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.IUserRepository;
import co.com.crediya.r2dbc.UserReactiveRepository;
import co.com.crediya.r2dbc.UserReactiveRepositoryAdapter;
import co.com.crediya.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;

class ReactiveAdapterOperationsTest {

    private UserReactiveRepository repository;
    private ObjectMapper mapper;
    private TransactionalOperator txOperator;
    private ReactiveAdapterOperations<User, UserEntity, Long, UserReactiveRepository> operations;


    @BeforeEach
    void setUp() {
        repository = Mockito.mock(UserReactiveRepository.class);
        txOperator = Mockito.mock(TransactionalOperator.class);
        mapper = Mockito.mock(ObjectMapper.class);
        operations = new ReactiveAdapterOperations<>(repository, mapper, d -> {
            User u = new User();
            u.setId(d.getId());
            u.setEmail(d.getEmail());
            return u;
        }, txOperator) {
        };
    }

    @Test
    void save() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setEmail("email");

        User user = new User();
        user.setId(1L);
        user.setEmail("email");

        when(mapper.map(user, UserEntity.class))
                .thenReturn(entity);
        when(repository.save(any(UserEntity.class)))
                .thenReturn(Mono.just(entity));
        when(txOperator.transactional(any(Mono.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        StepVerifier.create(operations.save(user))
                .expectNextMatches(u ->
                        u.getId().equals(user.getId()) &&
                                u.getEmail().equals(user.getEmail()))
                .verifyComplete();
    }


    @Test
    void saveAllEntities() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setEmail("email");

        User user = new User();
        user.setId(1L);
        user.setEmail("email");

        when(mapper.map(user, UserEntity.class))
                .thenReturn(entity);
        when(repository.saveAll(any(Flux.class)))
                .thenReturn(Flux.just(entity));
        when(txOperator.transactional(any(Flux.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        StepVerifier.create(operations.saveAllEntities(Flux.just(user)))
                .expectNextMatches(u ->
                        u.getId().equals(user.getId()) &&
                                u.getEmail().equals(user.getEmail()))
                .verifyComplete();
    }

    @Test
    void findById() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setEmail("email");

        User user = new User();
        user.setId(1L);
        user.setEmail("email");

        when(mapper.map(user, UserEntity.class))
                .thenReturn(entity);
        when(repository.findById(any(Long.class)))
                .thenReturn(Mono.just(entity));
        when(txOperator.transactional(any(Mono.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        StepVerifier.create(operations.findById(any(Long.class)))
                .expectNextMatches(u ->
                        u.getId().equals(user.getId()) &&
                                u.getEmail().equals(user.getEmail()))
                .verifyComplete();
    }

    @Test
    void findAll() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setEmail("email");

        User user = new User();
        user.setId(1L);
        user.setEmail("email");

        when(mapper.map(user, UserEntity.class))
                .thenReturn(entity);
        when(repository.findAll())
                .thenReturn(Flux.just(entity));
        when(txOperator.transactional(any(Flux.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        StepVerifier.create(operations.findAll())
                .expectNextMatches(u ->
                        u.getId().equals(user.getId()) &&
                                u.getEmail().equals(user.getEmail()))
                .verifyComplete();
    }
}
