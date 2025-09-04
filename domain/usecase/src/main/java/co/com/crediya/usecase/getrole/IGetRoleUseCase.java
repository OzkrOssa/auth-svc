package co.com.crediya.usecase.getrole;

import co.com.crediya.model.role.Role;
import reactor.core.publisher.Mono;

public interface IGetRoleUseCase {
    Mono<Role>execute(Long id);
}
