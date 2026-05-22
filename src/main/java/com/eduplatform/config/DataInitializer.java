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
                        .instructor("Carlos Ramírez")
                        .durationHours(40)
                        .cost(new BigDecimal("49.99"))
                        .description("Aprende a crear APIs REST profesionales con Spring Boot 3")
                        .category("Backend")
                        .maxStudents(50)
                        .available(true)
                        .build(),
                    Course.builder()
                        .name("React.js Avanzado")
                        .instructor("María González")
                        .durationHours(35)
                        .cost(new BigDecimal("59.99"))
                        .description("Domina React hooks, Context API y patrones avanzados")
                        .category("Frontend")
                        .maxStudents(40)
                        .available(true)
                        .build(),
                    Course.builder()
                        .name("Python para Data Science")
                        .instructor("Andrés Torres")
                        .durationHours(50)
                        .cost(new BigDecimal("69.99"))
                        .description("Análisis de datos con Pandas, NumPy y Matplotlib")
                        .category("Data Science")
                        .maxStudents(60)
                        .available(true)
                        .build(),
                    Course.builder()
                        .name("Docker & Kubernetes")
                        .instructor("Laura Mendoza")
                        .durationHours(30)
                        .cost(new BigDecimal("44.99"))
                        .description("Contenedores y orquestación para aplicaciones modernas")
                        .category("DevOps")
                        .maxStudents(35)
                        .available(true)
                        .build(),
                    Course.builder()
                        .name("Base de Datos Oracle Cloud")
                        .instructor("Roberto Sánchez")
                        .durationHours(25)
                        .cost(new BigDecimal("39.99"))
                        .description("Administración y desarrollo con Oracle Autonomous Database")
                        .category("Database")
                        .maxStudents(30)
                        .available(true)
                        .build(),
                    Course.builder()
                        .name("Microservicios con Spring Cloud")
                        .instructor("Carlos Ramírez")
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
