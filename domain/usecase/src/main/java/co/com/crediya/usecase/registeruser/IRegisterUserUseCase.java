package co.com.crediya.usecase.registeruser;

import co.com.crediya.model.user.User;
import reactor.core.publisher.Mono;

public interface IRegisterUserUseCase {
    Mono<User> execute(User user);
}
