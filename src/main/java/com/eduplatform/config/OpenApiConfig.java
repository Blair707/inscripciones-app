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
                        .title("InscripcionesApp - API de Inscripción de Cursos")
                        .version("1.0.0")
                        .description("""
                                REST API for managing virtual course enrollment.
                                
                                ## Features
                                - 📚 List available courses with details (name, instructor, duration, cost)
                                - ➕ Add new courses to the educational catalog
                                - 🎓 Enroll students in one or more courses
                                - 🧾 Generate enrollment summaries with total cost calculation
                                - 🏷️ Automatic discounts: 5% for 2 courses, 10% for 3 or more
                                - 🔍 Filter courses by category, keyword, and price range
                                
                                ## Payment Methods
                                CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, PAYPAL, CASH
                                """)
                        .contact(new Contact()
                                .name("InscripcionesApp Team")
                                .email("dev@eduplatform.com"))
                        .license(new License()
                                .name("MIT License")))
                .servers(List.of(
                        new Server().url("/").description("Current server")
                ));
    }
}
