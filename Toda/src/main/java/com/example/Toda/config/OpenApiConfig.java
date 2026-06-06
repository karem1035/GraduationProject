package com.example.Toda.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Toda API")
                        .version("1.0")
                        .description("Tour Guide Application API"))
                .tags(List.of(
                        new Tag().name("01. Marketplace — Tour Guides").description("Public endpoint to list all available tour guides"),
                        new Tag().name("01. Marketplace — Static Trips").description("Static trips created by any user, viewable by everyone, bookable by tourists"),
                        new Tag().name("01. Marketplace — Guide Booking Requests").description("Tourists can request custom bookings from tour guides; guides can accept or reject")
                ))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token obtained from signup/login endpoint")));
    }
}