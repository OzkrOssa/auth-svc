package co.com.crediya.model.role.gateways;

import co.com.crediya.model.role.Role;
import reactor.core.publisher.Mono;

public interface IRoleRepository {
    Mono<Role>findById(Long id);
}
