package co.com.crediya.usecase.login;

import reactor.core.publisher.Mono;

public interface ILoginUseCase {
    Mono<String> execute(String email, String password);
}
