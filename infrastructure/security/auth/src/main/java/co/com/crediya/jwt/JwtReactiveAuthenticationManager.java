package co.com.crediya.jwt;

import co.com.crediya.model.tokenprovider.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final TokenProvider jwtTokenProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        if (!jwtTokenProvider.validateToken(token)) {
            return Mono.empty();
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        String role = jwtTokenProvider.getRoleFromToken(token);

        return Mono.just(new UsernamePasswordAuthenticationToken(
                email,
                token,
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
        ));
    }
}