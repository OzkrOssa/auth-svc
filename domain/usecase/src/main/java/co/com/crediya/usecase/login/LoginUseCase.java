package co.com.crediya.usecase.login;

import co.com.crediya.model.passwordencoder.PasswordEncoder;
import co.com.crediya.model.role.gateways.IRoleRepository;
import co.com.crediya.model.tokenprovider.TokenProvider;
import co.com.crediya.model.user.gateways.IUserRepository;
import co.com.crediya.usecase.login.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUseCase implements ILoginUseCase {

    private final IUserRepository repo;
    private final IRoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Override
    public Mono<String> execute(String email, String password) {
        return repo.findByEmail(email)
                .switchIfEmpty(Mono.error(new InvalidCredentialsException("Invalid email or password")))
                .flatMap(user -> {
                    if (!passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.error(new InvalidCredentialsException("Invalid email or password"));
                    }
                    return roleRepo.findById(user.getRoleId())
                            .map(role -> tokenProvider.generateToken(email, role.getName().toUpperCase()));
                });
    }
}

