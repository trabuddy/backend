package com.ssafy.trabuddy.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition(
        servers = @io.swagger.v3.oas.annotations.servers.Server(url = "/api")
)
class SwaggerConfig {
    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("서버 URL - 개발 환경");

        Contact contact = new Contact();
        contact.setName("API 지원팀");
        contact.setEmail("support@example.com");
        contact.setUrl("https://www.example.com");

        License mitLicense = new License()
                .name("MIT 라이센스")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("애플리케이션 API")
                .version("1.0")
                .contact(contact)
                .description("이 API는 애플리케이션의 기능을 제공합니다.")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(new Components().addSecuritySchemes("JWT", createAPIKeyScheme()))
                .servers(List.of(devServer));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}