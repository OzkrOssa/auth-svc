package co.com.crediya.api.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Auth Service")
                        .version("v1")
                        .description("Swagger Auth Service Documentation"))
                .externalDocs(new ExternalDocumentation()
                        .description("Github Repository")
                        .url("https://github.com/OzkrOssa/auth-svc"));
    }
}