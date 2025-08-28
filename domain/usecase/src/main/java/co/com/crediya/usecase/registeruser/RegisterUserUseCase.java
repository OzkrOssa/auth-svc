package co.com.crediya.usecase.registeruser;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.IUserRepository;
import co.com.crediya.usecase.registeruser.exception.EmailAlreadyExistsException;
import co.com.crediya.usecase.registeruser.exception.InvalidBaseSalaryException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegisterUserUseCase implements IRegisterUserUseCase {
    private final IUserRepository repo;

    @Override
    public Mono<User> execute(User user) {
        Mono<Boolean> emailCheck = repo.existByEmail(user.getEmail());
        Mono<Boolean> salaryCheck = Mono.just(user.isBaseSalaryValid());

        return Mono.zip(emailCheck, salaryCheck)
                .flatMap(tuple -> {
                    Boolean emailExists = tuple.getT1();
                    Boolean salaryValid = tuple.getT2();

                    if (emailExists) return Mono.error(new EmailAlreadyExistsException("email already exists"));
                    if (!salaryValid) return Mono.error(new InvalidBaseSalaryException("base salary not valid, must be between 0 and 15,000,000"));

                    return repo.save(user);
                });
    }
}
