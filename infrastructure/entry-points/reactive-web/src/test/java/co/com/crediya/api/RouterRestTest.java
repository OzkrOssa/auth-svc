package co.com.crediya.api;

import co.com.crediya.api.config.WebPropertiesConfig;
import co.com.crediya.api.dto.ResponseDto;
import co.com.crediya.api.dto.UserRequestDto;
import co.com.crediya.api.exception.GlobalExceptionHandler;
import co.com.crediya.model.user.User;
import co.com.crediya.usecase.getuser.IGetUserUseCase;
import co.com.crediya.usecase.getuser.exception.UserNotFoundException;
import co.com.crediya.usecase.getusers.IGetUsersUseCase;
import co.com.crediya.usecase.getusers.exception.UsersNotFoundException;
import co.com.crediya.usecase.login.ILoginUseCase;
import co.com.crediya.usecase.login.exception.InvalidCredentialsException;
import co.com.crediya.usecase.registeruser.IRegisterUserUseCase;
import org.springframework.context.annotation.Import;
import org.springframework.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.BindingResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;


import java.util.List;
import java.util.Map;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
@Import({GlobalExceptionHandler.class, WebPropertiesConfig.class})
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;


    @MockitoBean
    private IRegisterUserUseCase registerUserUseCase;
    @MockitoBean
    private IGetUserUseCase getUserUseCase;
    @MockitoBean
    private IGetUsersUseCase getUsersUseCase;

    @MockitoBean
    private ILoginUseCase loginUseCase;

    @MockitoBean
    private Validator validator;


    private UserRequestDto validUserRequest;
    private UserRequestDto invalidUserRequest;


    @BeforeEach
    void setUp() {
        validUserRequest = new UserRequestDto();
        validUserRequest.setFirstName("test");
        validUserRequest.setLastName("test");
        validUserRequest.setEmail("test@test.com");
        validUserRequest.setPhone("test");
        validUserRequest.setBaseSalary(10000L);

        invalidUserRequest = new UserRequestDto();
        invalidUserRequest.setFirstName("test");
        invalidUserRequest.setLastName("test");
        invalidUserRequest.setEmail("invalid_email");
        invalidUserRequest.setPhone("test");
        invalidUserRequest.setBaseSalary(-1L);
    }


    @Test
    void health_OK() {
        webTestClient.get()
                .uri("/api/v1/health")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .value(response -> {
                    Assertions.assertThat(response)
                            .containsEntry("server_status", "OK")
                            .containsEntry("service", "AuthService");
                    Assertions.assertThat(response.get("timestamp")).isNotNull();
                });
    }

    @Test
    void test_RegisterUser_WithValidData_ShouldReturnSuccess() {
        User user = User.builder()
                .firstName(validUserRequest.getFirstName())
                .lastName(validUserRequest.getLastName())
                .email(validUserRequest.getEmail())
                .phone(validUserRequest.getPhone())
                .baseSalary(validUserRequest.getBaseSalary())
                .build();

        doAnswer(invocation -> {
            invocation.getArgument(1);
            return null;
        }).when(validator).validate(eq(invalidUserRequest), any(BindingResult.class));

        when(registerUserUseCase.execute(any(User.class))).thenReturn(Mono.just(user));
        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validUserRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseDto.class)
                .value(response -> {
                    Assertions.assertThat(response.isSuccess()).isTrue();
                    Assertions.assertThat(response.getMessage()).isEqualTo("User created successfully");
                });
    }

    @Test
    void Test_RegisterUser_WithInvalidEmail_ShouldReturnBadRequest() {
        doAnswer(invocation -> {
            BindingResult errors = invocation.getArgument(1);
            errors.rejectValue("email", "email.invalid", "email must be valid");
            return null;
        }).when(validator).validate(any(UserRequestDto.class), any(BindingResult.class));

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidUserRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ResponseDto.class)
                .value(response -> {
                    Assertions.assertThat(response.isSuccess()).isFalse();
                    Assertions.assertThat(response.getMessage()).isEqualTo("Validation error");
                    Assertions.assertThat(response.getErrors()).isNotNull().contains("email must be valid");
                });

    }

    @Test
    void test_RegisterUser_WithNegativeSalary_ShouldReturnBadRequest() {

        invalidUserRequest.setBaseSalary(-10L);

        doAnswer(invocation -> {
            BindingResult errors = invocation.getArgument(1);
            errors.rejectValue("baseSalary", "baseSalary.invalid", "base salary must be positive");
            return null;
        }).when(validator).validate(any(UserRequestDto.class), any(BindingResult.class));

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidUserRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ResponseDto.class)
                .value(response -> {
                    Assertions.assertThat(response.isSuccess()).isFalse();
                    Assertions.assertThat(response.getMessage()).isEqualTo("Validation error");
                    Assertions.assertThat(response.getErrors())
                            .contains("base salary must be positive");
                });

    }

    @Test
    void test_RegisterUser_WithInvalidSalary_ShouldReturnBadRequest() {

        invalidUserRequest.setBaseSalary(10000000000000L);

        doAnswer(invocation -> {
            BindingResult errors = invocation.getArgument(1);
            errors.rejectValue("baseSalary", "baseSalary.invalid", "base salary must be less than or equal 15,000,000");
            return null;
        }).when(validator).validate(any(UserRequestDto.class), any(BindingResult.class));

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidUserRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ResponseDto.class)
                .value(response -> {
                    Assertions.assertThat(response.isSuccess()).isFalse();
                    Assertions.assertThat(response.getMessage()).isEqualTo("Validation error");
                    Assertions.assertThat(response.getErrors())
                            .contains("base salary must be less than or equal 15,000,000");
                });

    }

    @Test
    void test_GetUser_WithValidId_ShouldReturnUser() {
        String userEmail = validUserRequest.getEmail();

        User user = User.builder()
                .firstName(validUserRequest.getFirstName())
                .lastName(validUserRequest.getLastName())
                .email(validUserRequest.getEmail())
                .phone(validUserRequest.getPhone())
                .baseSalary(validUserRequest.getBaseSalary())
                .build();


        when(getUserUseCase.execute(any(String.class)))
                .thenReturn(Mono.just(user));

        webTestClient.get()
                .uri("/api/v1/users"+"/{email}", userEmail)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseDto.class)
                .value(response -> {
                    Assertions.assertThat(response.isSuccess()).isTrue();
                    Assertions.assertThat(response.getMessage()).isEqualTo("User retrieved successfully");
                });
    }

    @Test
    void test_GetUser_WithInvalidId_ShouldReturnNotFound() {
        String userEmail = validUserRequest.getEmail();

        when(getUserUseCase.execute(userEmail))
                .thenReturn(Mono.error(new UserNotFoundException("user not found with id: " + userEmail)));


        webTestClient.get()
                .uri("/api/v1/users"+"/{id}", userEmail)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ResponseDto.class)
                .value(response -> {
                    Assertions.assertThat(response.isSuccess()).isFalse();
                    Assertions.assertThat(response.getMessage()).isEqualTo("Not Found");
                });
    }

    @Test
    void test_GetUsers_ShouldReturnUsers() {
        User user = User.builder()
                .id(1L)
                .firstName("test")
                .lastName("test")
                .email("test@example.com")
                .build();

        when(getUsersUseCase.execute())
                .thenReturn(Flux.just(user));


        webTestClient.get()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseDto.class)
                .value(response -> {
                    Assertions.assertThat(response.isSuccess()).isTrue();
                    Assertions.assertThat(response.getMessage()).isEqualTo("Users retrieved successfully");
                });
    }

    @Test
    void test_GetUsers_ShouldReturnEmptyList() {
        when(getUsersUseCase.execute())
                .thenReturn(Flux.error(new UsersNotFoundException("No users found")));

        webTestClient.get()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ResponseDto.class)
                .value(response -> {
                    Assertions.assertThat(response.isSuccess()).isFalse();
                    Assertions.assertThat(response.getMessage()).isEqualTo("Not Found");
                    Assertions.assertThat(response.getData()).isNull();
                });
    }

    @Test
    void test_Login_WithValidCredentials_ShouldReturnSuccess() {
        String email = "test@examplecom";
        String password = "password123";

        when(loginUseCase.execute(email, password))
                .thenReturn(Mono.just("mocked_jwt_token"));

        webTestClient.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("email", email, "password", password))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseDto.class)
                .value(response -> {
                    Assertions.assertThat(response.isSuccess()).isTrue();
                    Assertions.assertThat(response.getMessage()).isEqualTo("Login successfully");
                    Assertions.assertThat(response.getData()).isEqualTo(Map.of("token", "mocked_jwt_token"));
                });
    }

    @Test
    void test_Login_WithInvalidCredentials_ShouldReturnUnauthorized() {
        String email = "test@examplecom";
        String password = "wrongpassword";

        when(loginUseCase.execute(email, password))
                .thenReturn(Mono.error(new InvalidCredentialsException("Invalid email or password")));

        webTestClient.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("email", email, "password", password))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(ResponseDto.class)
                .value(response -> {
                    Assertions.assertThat(response.isSuccess()).isFalse();
                    Assertions.assertThat(response.getMessage()).isEqualTo("Unauthorized");
                    Assertions.assertThat(response.getData()).isNull();
                    Assertions.assertThat(response.getErrors()).isEqualTo(List.of("Invalid email or password"));
                });
    }
}
