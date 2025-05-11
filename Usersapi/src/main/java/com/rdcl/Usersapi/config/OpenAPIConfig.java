package com.rdcl.Usersapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("Usersapi API").description("Documentación de los endpoints para UsersapiApplication").version("1.0.0").contact(new Contact().name("Ruben Dario Carrillo").email("rubendario921@hotmail.com")));

    }
}
