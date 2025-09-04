package co.com.crediya.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("/api/v1/health"), handler::health)
                .andRoute(POST("/api/v1/users"), handler::registerUser)
                .and(route(GET("/api/v1/users/{email}"), handler::getUser))
                .and(route(GET("/api/v1/users"), handler::getUsers))
                .andRoute(POST("/api/v1/auth/login"), handler::login);
    }
}
