package co.com.crediya.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServerAuthenticationConverterTest {

    private JwtServerAuthenticationConverter converter;

    @BeforeEach
    void setUp() {
        converter = new JwtServerAuthenticationConverter();
    }

    @Test
    void givenValidBearerToken_whenConvert_thenReturnAuthentication() {
        String token = "jwt.token.value";
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        Mono<Authentication> result = converter.convert(exchange);

        StepVerifier.create(result)
                .assertNext(auth -> {
                    assertThat(auth).isInstanceOf(UsernamePasswordAuthenticationToken.class);
                    assertThat(auth.getPrincipal()).isNull();
                    assertThat(auth.getCredentials()).isEqualTo(token);
                })
                .verifyComplete();
    }

    @Test
    void givenMissingAuthorizationHeader_whenConvert_thenReturnEmpty() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/test").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        Mono<Authentication> result = converter.convert(exchange);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void givenInvalidAuthorizationHeader_whenConvert_thenReturnEmpty() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/test")
                .header(HttpHeaders.AUTHORIZATION, "Basic abc123")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        Mono<Authentication> result = converter.convert(exchange);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
