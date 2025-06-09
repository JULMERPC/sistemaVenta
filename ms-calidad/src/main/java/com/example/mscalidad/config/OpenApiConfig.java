package com.example.mscalidad.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msCalidadOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Microservicio Calidad API")
                        .description("API para el control de calidad de productos")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("hector")
                                .email("pacompiahectorrobert@gmail.com")
                                .url("https://actisport.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentación del proyecto")
                        .url("https://github.com/hectorpq/distribuidas.git"));
    }
}
