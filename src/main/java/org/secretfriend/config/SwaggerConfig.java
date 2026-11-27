package org.secretfriend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${spring.application.version:unknown}")
    private String version;

    @Bean
    public OpenAPI openAPI() {

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Secret Friend Backend")
                        .description("User, Session and Participants management for Secret Friend game")
                        .version(version)
                        .contact(new Contact()
                                .name("Lara Martuscelli")
                                .email("laramartuscelli@gmail.com")
                        )

                ).components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Please, inform the generated JWT Token (without 'Bearer') on the /login endpoint")
                        )
                ).addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }
}
