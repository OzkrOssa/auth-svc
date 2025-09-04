package co.com.crediya.usecase.getuser;

import co.com.crediya.model.user.User;
import reactor.core.publisher.Mono;

public interface IGetUserUseCase {
    Mono<User> execute(String email);
}
