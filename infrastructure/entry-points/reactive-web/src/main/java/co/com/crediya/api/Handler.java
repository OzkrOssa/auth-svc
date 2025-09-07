package co.com.crediya.api;

import co.com.crediya.api.dto.LoginDto;
import co.com.crediya.api.dto.ResponseDto;
import co.com.crediya.api.dto.UserRequestDto;
import co.com.crediya.api.mapper.UserMapper;
import co.com.crediya.model.user.User;
import co.com.crediya.usecase.getuser.IGetUserUseCase;
import co.com.crediya.usecase.getusers.IGetUsersUseCase;
import co.com.crediya.usecase.login.ILoginUseCase;
import co.com.crediya.usecase.registeruser.IRegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private final IRegisterUserUseCase registerUserUseCase;
    private final IGetUserUseCase getUserUseCase;
    private final IGetUsersUseCase getUsersUseCase;
    private final ILoginUseCase loginUseCase;

    private final Validator validator;

    public Mono<ServerResponse> health(ServerRequest request){
        Map<String, Object> status = Map.of(
                "server_status", "OK",
                "timestamp", Instant.now().toString(),
                "service", "AuthService"
        );
        return ServerResponse.ok().bodyValue(status);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR')")
    public Mono<ServerResponse> registerUser(ServerRequest request) {
        return request.bodyToMono(UserRequestDto.class)
                .flatMap(dto -> {
                    BindingResult errors = new BeanPropertyBindingResult(dto, "userRequestDto");
                    validator.validate(dto, errors);

                    if (errors.hasErrors()) {
                        var errorMessages = errors.getAllErrors().stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .toList();

                        ResponseDto<Void> response = ResponseDto.<Void>builder()
                                .success(false)
                                .message("Validation error")
                                .errors(errorMessages)
                                .build();

                        return ServerResponse.badRequest()
                                .bodyValue(response);
                    }

                    User user = UserMapper.toDomain(dto);
                    return registerUserUseCase.execute(user)
                            .flatMap(savedUser -> ServerResponse.ok().bodyValue(
                                    ResponseDto.builder()
                                            .success(true)
                                            .message("User created successfully")
                                            .data(savedUser)
                                            .build()
                            ));
                });

    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR')")
    public Mono<ServerResponse> getUser(ServerRequest request) {
        String email = request.pathVariable("email");

        return getUserUseCase.execute(email)
                .flatMap(user -> ServerResponse.ok().bodyValue(
                        ResponseDto.builder()
                                .success(true)
                                .message("User retrieved successfully")
                                .data(user)
                                .build()
                ));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR')")
    public Mono<ServerResponse> getUsers(ServerRequest request) {
        return getUsersUseCase.execute()
                .collectList()
                .flatMap(users -> ServerResponse.ok().bodyValue(
                        ResponseDto.builder()
                                .success(true)
                                .message("Users retrieved successfully")
                                .data(users)
                                .build()
                ));
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginDto.class)
                .flatMap(dto -> {
                    BindingResult errors = new BeanPropertyBindingResult(dto, "loginDto");
                    validator.validate(dto, errors);

                    if (errors.hasErrors()) {
                        var errorMessages = errors.getAllErrors().stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .toList();

                        ResponseDto<Void> response = ResponseDto.<Void>builder()
                                .success(false)
                                .message("Validation error")
                                .errors(errorMessages)
                                .build();

                        return ServerResponse.badRequest()
                                .bodyValue(response);
                    }

                    return loginUseCase.execute(dto.getEmail(), dto.getPassword())
                            .flatMap(generatedToken -> {
                                ResponseDto<Map<String,String>> response = ResponseDto.<Map<String,String>>builder()
                                        .success(true)
                                        .message("Login successfully")
                                        .data(Map.of("token", generatedToken))
                                        .build();
                                return ServerResponse.ok().bodyValue(response);
                            });
                });
    }
}