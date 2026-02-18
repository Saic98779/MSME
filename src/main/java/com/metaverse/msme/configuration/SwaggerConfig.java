package com.metaverse.msme.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${app.swagger.server-url:}")
    private String swaggerServerUrl;

    @Value("${app.swagger.server-description:}")
    private String swaggerServerDescription;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "Bearer Authentication";

        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("MSME API Documentation")
                        .description("REST API documentation for MSME Management System with JWT Authentication")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("MSME Development Team")
                                .email("support@msme.com")
                                .url("https://msme.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT Bearer token in the format: Bearer <token>")));

        if (swaggerServerUrl != null && !swaggerServerUrl.isBlank()) {
            String description = (swaggerServerDescription == null || swaggerServerDescription.isBlank())
                    ? "Configured Server"
                    : swaggerServerDescription;
            openAPI.servers(List.of(new Server().url(swaggerServerUrl).description(description)));
        }

        return openAPI;
    }
}
