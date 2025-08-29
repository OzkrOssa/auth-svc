package co.com.crediya.api.config;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class SwaggerConfigTest {

    private final SwaggerConfig config = new SwaggerConfig();

    @Test
    void customOpenAPI_ShouldReturnConfiguredOpenAPI() {
        var openAPI = config.customOpenAPI();

        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo().getTitle()).isEqualTo("Auth Service");
        assertThat(openAPI.getInfo().getVersion()).isEqualTo("v1");
        assertThat(openAPI.getInfo().getDescription()).isEqualTo("Swagger Auth Service Documentation");
        assertThat(openAPI.getExternalDocs().getDescription()).isEqualTo("Github Repository");
        assertThat(openAPI.getExternalDocs().getUrl()).isEqualTo("https://github.com/OzkrOssa/auth-svc");
    }
}

