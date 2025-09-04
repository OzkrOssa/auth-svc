package co.com.crediya.usecase.getrole;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.IRoleRepository;
import co.com.crediya.usecase.getrole.exception.InvalidRolException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetRoleUseCase implements IGetRoleUseCase {
    private final IRoleRepository rolRepo;

    @Override
    public Mono<Role> execute(Long id) {
        return rolRepo.findById(id)
                .switchIfEmpty(Mono.error(new InvalidRolException("Rol not found with id: " + id)));
    }
}
