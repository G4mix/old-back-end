package com.gamix.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

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
        .components(new Components()
            .addSecuritySchemes("jwt", securityScheme)
        );
    }

}
