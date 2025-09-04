package co.com.crediya.r2dbc;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.IUserRepository;
import co.com.crediya.r2dbc.entity.UserEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        Long,
        UserReactiveRepository
        > implements IUserRepository {
    private final TransactionalOperator txOperator;

    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper, TransactionalOperator txOperator) {
        super(repository, mapper, d -> User.builder()
                .id(d.getId())
                .firstName(d.getFirstName())
                .lastName(d.getLastName())
                .email(d.getEmail())
                .document(d.getDocument())
                .phone(d.getPhone())
                .baseSalary(d.getBaseSalary())
                .roleId(d.getRoleId())
                .build(), txOperator);
        this.txOperator = txOperator;
    }

    @Override
    public Mono<Boolean> existByEmail(String email) {
        return repository.existsByEmail(email)
                .as(txOperator::transactional);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .as(txOperator::transactional);
    }
}