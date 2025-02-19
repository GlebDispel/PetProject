package ru.glebdos.usermicroservice.config;

import io.swagger.v3.oas.models.OpenAPI;

import io.swagger.v3.parser.OpenAPIV3Parser;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new  OpenAPIV3Parser().read("openapi.yaml");

    }
    @Bean
    public GroupedOpenApi customApi() {
        return GroupedOpenApi.builder()
                .group("custom-api")
                .pathsToMatch("/users/registration","/users/search","/auth","/users/update")
                .build();
    }

}