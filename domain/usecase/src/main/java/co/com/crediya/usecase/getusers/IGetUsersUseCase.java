package co.com.crediya.usecase.getusers;

import co.com.crediya.model.user.User;
import reactor.core.publisher.Flux;

public interface IGetUsersUseCase {
    Flux<User> execute();
}
