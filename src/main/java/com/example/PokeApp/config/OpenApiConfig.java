package com.example.PokeApp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pokemon API")
                        .version("1.0")
                        .description("This is a sample Spring Boot service for fetching Pokemon data.")
                        .contact(new Contact().name("Ash").email("Ash@oraniaCity.com")));
    }
}
