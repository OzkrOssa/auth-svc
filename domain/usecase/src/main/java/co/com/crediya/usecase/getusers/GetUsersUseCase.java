package co.com.crediya.usecase.getusers;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.IUserRepository;
import co.com.crediya.usecase.getusers.exception.UsersNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetUsersUseCase implements IGetUsersUseCase {
    private final IUserRepository repo;

    @Override
    public Flux<User> execute() {
        return repo.findAll()
                .switchIfEmpty(Flux.error(new UsersNotFoundException("No users found")));
    }
}
