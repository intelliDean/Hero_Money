package com.api.xpress;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


//@EnableWebMvc
@Configuration
public class OpenAPIConfig {

    private static final String BEARER_AUTH = "Bearer Auth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(buildInfo())
                .addSecurityItem(buildSecurityRequirement())
                .components(buildComponents())
                .servers(List.of(buildServer()));
    }

    private Server buildServer() {
        return new Server()
                .url("/")
                .description("Current environment");
    }

    private Info buildInfo() {
        return new Info()
                .title("Xpress APIs")
                .version("1.0.0")
                .description("REST API documentation for the Xpress platform")
                .contact(buildContact())
                .termsOfService("https://xpress.com/terms");
    }

    private Contact buildContact() {
        return new Contact()
                .name("Xpress Support — Michael Dean")
                .email("o.michaeldean@gmail.com")
                .url("https://www.linkedin.com/in/michaeldean8ix/");
    }

    private SecurityRequirement buildSecurityRequirement() {
        return new SecurityRequirement()
                .addList(BEARER_AUTH);
    }

    private Components buildComponents() {
        return new Components()
                .addSecuritySchemes(BEARER_AUTH, buildSecurityScheme());
    }

    private SecurityScheme buildSecurityScheme() {
        return new SecurityScheme()
                .name(BEARER_AUTH)
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Paste your JWT token below — the `Bearer` prefix is added automatically");
    }

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/api/**")
                .build();
    }
}
