package com.eduplatform.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("InscripcionesApp - API de Inscripcion de Cursos")
                        .version("1.0.0")
                        .description("REST API para la gestion de inscripciones en cursos virtuales. " +
                                "Permite listar cursos, agregar nuevos cursos e inscribir estudiantes " +
                                "con calculo automatico de descuentos y resumen de pago.")
                        .contact(new Contact()
                                .name("InscripcionesApp Team")
                                .email("dev@inscripcionesapp.com"))
                        .license(new License()
                                .name("MIT License")))
                .servers(List.of(
                        new Server().url("/").description("Current server")
                ));
    }
}
