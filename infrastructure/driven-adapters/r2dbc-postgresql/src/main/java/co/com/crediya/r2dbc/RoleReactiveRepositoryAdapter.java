package co.com.crediya.r2dbc;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.IRoleRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.IUserRepository;
import co.com.crediya.r2dbc.entity.RoleEntity;
import co.com.crediya.r2dbc.entity.UserEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
public class RoleReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Role,
        RoleEntity,
        Long,
        RoleReactiveRepository
        > implements IRoleRepository {
    private final TransactionalOperator txOperator;

    public RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper, TransactionalOperator txOperator) {
        super(repository, mapper, d -> Role.builder()
                .id(d.getId())
                .name(d.getName())
                .description(d.getDescription())
                .build(), txOperator);
        this.txOperator = txOperator;
    }
}
