package co.com.crediya.model.user.gateways;

import co.com.crediya.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserRepository {
    Mono<User> save(User user);
    Mono<User> findById(Long id);
    Mono<Boolean> existByEmail(String email);
    Flux<User> findAll();
}
