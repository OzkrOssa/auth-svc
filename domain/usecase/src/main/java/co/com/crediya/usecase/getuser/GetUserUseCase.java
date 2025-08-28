package co.com.crediya.usecase.getuser;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.IUserRepository;
import co.com.crediya.usecase.getuser.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetUserUseCase implements IGetUserUseCase {
    private final IUserRepository repo;

    @Override
    public Mono<User> execute(Long id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException("user not found with id: " + id)))
                .flatMap(Mono::just);
    }
}
