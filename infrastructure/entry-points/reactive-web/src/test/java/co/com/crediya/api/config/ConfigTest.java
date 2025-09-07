package co.com.crediya.api.config;

import co.com.crediya.api.Handler;
import co.com.crediya.api.RouterRest;
import co.com.crediya.config.SecurityConfig;
import co.com.crediya.model.tokenprovider.TokenProvider;
import co.com.crediya.usecase.getuser.IGetUserUseCase;
import co.com.crediya.usecase.getusers.IGetUsersUseCase;
import co.com.crediya.usecase.login.ILoginUseCase;
import co.com.crediya.usecase.registeruser.IRegisterUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.Validator;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class, SecurityConfig.class})
class ConfigTest {

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
    private TokenProvider tokenProvider;


    @MockitoBean
    private Validator validator;

    @Test
    void corsConfigurationShouldAllowOrigins() {
        webTestClient.get()
                .uri("/api/v1/health")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}