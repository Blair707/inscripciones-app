package com.eduplatform.config;

import com.eduplatform.model.Course;
import com.eduplatform.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    @Profile("!test")
    public CommandLineRunner initData(CourseRepository courseRepository) {
        return args -> {
            if (courseRepository.count() == 0) {
                log.info("Initializing demo course data...");
                List<Course> courses = List.of(
                    Course.builder()
                        .name("Java Spring Boot desde Cero")
                        .instructor("Mitsu Kahari")
                        .durationHours(40)
                        .cost(new BigDecimal("49.99"))
                        .description("Aprende a crear APIs REST profesionales con Spring Boot 3")
                        .category("Backend")
                        .maxStudents(50)
                        .available(true)
                        .build(),
                    Course.builder()
                        .name("React.js Avanzado")
                        .instructor("Alonso Yañez")
                        .durationHours(35)
                        .cost(new BigDecimal("59.99"))
                        .description("Domina React hooks, Context API y patrones avanzados")
                        .category("Frontend")
                        .maxStudents(40)
                        .available(true)
                        .build(),
                    Course.builder()
                        .name("Python para Data Science")
                        .instructor("Vicente Villegas")
                        .durationHours(50)
                        .cost(new BigDecimal("69.99"))
                        .description("Analisis de datos con Pandas, NumPy y Matplotlib")
                        .category("Data Science")
                        .maxStudents(60)
                        .available(true)
                        .build(),
                    Course.builder()
                        .name("Docker y Kubernetes")
                        .instructor("Benjamin Yañez")
                        .durationHours(30)
                        .cost(new BigDecimal("44.99"))
                        .description("Contenedores y orquestacion para aplicaciones modernas")
                        .category("DevOps")
                        .maxStudents(35)
                        .available(true)
                        .build(),
                    Course.builder()
                        .name("Base de Datos con PostgreSQL")
                        .instructor("Keznit Deus")
                        .durationHours(25)
                        .cost(new BigDecimal("39.99"))
                        .description("Administracion y desarrollo con PostgreSQL")
                        .category("Database")
                        .maxStudents(30)
                        .available(true)
                        .build(),
                    Course.builder()
                        .name("Microservicios con Spring Cloud")
                        .instructor("Mitsu Kahari")
                        .durationHours(45)
                        .cost(new BigDecimal("74.99"))
                        .description("Arquitectura de microservicios, Eureka, API Gateway y Circuit Breaker")
                        .category("Backend")
                        .maxStudents(45)
                        .available(true)
                        .build()
                );
                courseRepository.saveAll(courses);
                log.info("Initialized {} demo courses", courses.size());
            }
        };
    }
}
