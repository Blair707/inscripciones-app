package com.eduplatform.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
                        .version("3.0")
                        .description(
                                "Pipeline de despliegue automatico con integracion continua via GitHub Actions, " +
                                "Docker Hub y AWS EC2.\n\n" +
                                "Listado de cursos disponibles con consultas eficientes y manejo de errores.\n\n" +
                                "Registro de nuevos cursos con validacion de datos.\n\n" +
                                "Generacion de boletas de inscripcion con calculo de precio final, " +
                                "descuentos automaticos, multiples metodos de pago, " +
                                "validacion de cupos y cancelacion de inscripciones."
                        ))
                .servers(List.of(
                        new Server().url("/").description("Current server")
                ));
    }
}
