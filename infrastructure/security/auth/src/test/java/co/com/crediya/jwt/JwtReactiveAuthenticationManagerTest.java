package co.com.crediya.jwt;

import co.com.crediya.model.tokenprovider.TokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtReactiveAuthenticationManagerTest {

    @Mock
    private TokenProvider tokenProvider;
    @InjectMocks
    private JwtReactiveAuthenticationManager authManager;


    @Test
    void givenInvalidToken_whenAuthenticate_thenEmpty() {
        // arrange
        String token = "invalid.token";
        Authentication authentication = new UsernamePasswordAuthenticationToken(null, token);

        when(tokenProvider.validateToken(token)).thenReturn(false);

        // act
        Mono<Authentication> result = authManager.authenticate(authentication);

        // assert
        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(tokenProvider).validateToken(token);
        verifyNoMoreInteractions(tokenProvider);
    }

    @Test
    void givenValidToken_whenAuthenticate_thenReturnAuthentication() {
        // arrange
        String token = "valid.token";
        Authentication authentication = new UsernamePasswordAuthenticationToken(null, token);

        when(tokenProvider.validateToken(token)).thenReturn(true);
        when(tokenProvider.getEmailFromToken(token)).thenReturn("user@test.com");
        when(tokenProvider.getRoleFromToken(token)).thenReturn("USER");

        // act
        Mono<Authentication> result = authManager.authenticate(authentication);

        // assert
        StepVerifier.create(result)
                .assertNext(auth -> {
                    assertThat(auth).isInstanceOf(UsernamePasswordAuthenticationToken.class);
                    assertThat(auth.getPrincipal()).isEqualTo("user@test.com");
                    assertThat(auth.getCredentials()).isEqualTo(token);
                    assertThat(auth.getAuthorities()).extracting("authority")
                            .containsExactly("ROLE_USER");
                })
                .verifyComplete();

        verify(tokenProvider).validateToken(token);
        verify(tokenProvider).getEmailFromToken(token);
        verify(tokenProvider).getRoleFromToken(token);
    }
}
