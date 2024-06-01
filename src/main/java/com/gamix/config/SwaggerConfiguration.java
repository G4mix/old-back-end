package com.gamix.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");
        
        return new OpenAPI().info(
            new Info()
                .title("Gamix")
                .version("1.0.0")
                .description(
                    "The Gamix platform is a community that brings " +
                    "together game lovers, developers and enthusiasts " +
                    "to create innovative projects in the gaming area."
                )
        )
        .tags(
            List.of(
                new Tag().name("Auth").description("Endpoints related to the authentication in the Gamix"),
                new Tag().name("User").description("Endpoints related to the users."),
                new Tag().name("Post").description("Endpoints related to the posts."),
                new Tag().name("Comment").description("Endpoints related to the comments in posts, replays..."),
                new Tag().name("Like").description("Endpoints related to the likes in posts, comments...")
            )
        )
        .servers(
            List.of(
                new Server().url("http://localhost:8080").description("Localhost server"),
                new Server().url("http://192.168.99.100:8080").description("Localhost server to old versions of the Docker")
            )
        )
        .components(new Components()
            .addSecuritySchemes("jwt", securityScheme)
        );
    }

}
