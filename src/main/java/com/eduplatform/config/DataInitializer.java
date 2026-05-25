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
                log.info("Initializing course data...");
                List<Course> courses = List.of(
                    Course.builder()
                        .name("Herramientas DevStudio")
                        .instructor("Mitsu Kahari")
                        .durationHours(40)
                        .cost(new BigDecimal("25000"))
                        .description("Inicia tu creacion dentro del software Studio.")
                        .category("Backend")
                        .maxStudents(50)
                        .available(true)
                        .build(),
                    Course.builder()
                        .name("Tecnicas de modelaje 3D")
                        .instructor("Alonso Yañez")
                        .durationHours(35)
                        .cost(new BigDecimal("15000"))
                        .description("Aprende mejores tecnicas para modelar en Blender 3D.")
                        .category("Frontend")
                        .maxStudents(40)
                        .available(true)
                        .build(),
                    Course.builder()
                        .name("Payment Skills")
                        .instructor("Vicente Villegas")
                        .durationHours(50)
                        .cost(new BigDecimal("40000"))
                        .description("Descubre como funciona el Payment de Studio y gestiona tu futuro.")
                        .category("Data Science")
                        .maxStudents(60)
                        .available(true)
                        .build(),
                    Course.builder()
                        .name("Development Teams")
                        .instructor("Benjamin Yañez")
                        .durationHours(30)
                        .cost(new BigDecimal("10000"))
                        .description("Inicia este curso para encontrar gente con la que crear juntos.")
                        .category("DevOps")
                        .maxStudents(35)
                        .available(true)
                        .build(),
                    Course.builder()
                        .name("Introduccion a LUA")
                        .instructor("Keznit Deus")
                        .durationHours(25)
                        .cost(new BigDecimal("35000"))
                        .description("Aprende desde cero LUA.")
                        .category("Database")
                        .maxStudents(30)
                        .available(true)
                        .build(),
                    Course.builder()
                        .name("LUA de manera avanzada")
                        .instructor("Mitsu Kahari")
                        .durationHours(45)
                        .cost(new BigDecimal("65000"))
                        .description("Profundiza como LUA funciona en el mundo de la programacion.")
                        .category("Backend")
                        .maxStudents(45)
                        .available(true)
                        .build()
                );
                courseRepository.saveAll(courses);
                log.info("Initialized {} courses", courses.size());
            }
        };
    }
}
