package co.com.crediya.usecase.getusers;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.IUserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetUsersUseCase implements IGetUsersUseCase {
    private final IUserRepository repo;

    @Override
    public Flux<User> execute() {
        return repo.findAll();
    }
}
