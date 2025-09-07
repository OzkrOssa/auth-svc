package co.com.crediya.config;

import co.com.crediya.jwt.JwtReactiveAuthenticationManager;
import co.com.crediya.jwt.JwtServerAuthenticationConverter;
import co.com.crediya.model.tokenprovider.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            TokenProvider jwtTokenProvider) {

        JwtReactiveAuthenticationManager authManager = new JwtReactiveAuthenticationManager(jwtTokenProvider);
        JwtServerAuthenticationConverter converter = new JwtServerAuthenticationConverter();

        AuthenticationWebFilter jwtFilter = new AuthenticationWebFilter(authManager);
        jwtFilter.setServerAuthenticationConverter(converter);

        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/api/v1/auth/**",
                                "/api/v1/health",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/webjars/**"
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}