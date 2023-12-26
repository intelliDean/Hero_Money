package com.loan.hero;

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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@EnableWebMvc
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(info())
                .addSecurityItem(securityRequirement())
                .components(components())
                .servers(Collections.singletonList(server()));
    }

    @Bean
    public Server server() {
        return new Server()
                .url("/")
                .description("current");
    }

    private Info info() {
        return new Info()
                .title("Hero Money APIs")
                .version("Version 1.00")
                .description("This app provides REST APIs documentation for Hero Money")
                .contact(contact())
                .termsOfService("Terms of service");
    }

    @Bean
    public Contact contact() {
        return new Contact()
                .name("Michael Dean Oyewole")
                .email("o.michaeldean@gmail.com")
                .url("https://www.linkedin.com/in/michaeldean8ix/")
                .extensions(Map.of(
                                "Phone Number", "+23495729090",
                                "GitHub", "https://github.com/intelliDean"
                        )
                );
    }

    @Bean
    public SecurityRequirement securityRequirement() {
        return new SecurityRequirement()
                .addList("Bearer Auth", Arrays.asList("read", "write"));
    }

    @Bean
    public Components components() {
        return new Components()
                .addSecuritySchemes("Bearer Auth", securityScheme());
    }

    @Bean
    public SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Put the JWT Bearer Token in the Authorization header below to get authorized");
    }


    @Bean
    public GroupedOpenApi api() {
        String[] paths = {"/api/**"};
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch(paths)
                .build();
    }
}
