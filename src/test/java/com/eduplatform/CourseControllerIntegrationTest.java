package com.eduplatform;

import com.eduplatform.dto.CourseRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Course Controller Integration Tests")
class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/v1/courses - should return empty list initially")
    void shouldReturnAvailableCourses() throws Exception {
        mockMvc.perform(get("/api/v1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("POST /api/v1/courses - should create a new course")
    void shouldCreateCourse() throws Exception {
        CourseRequestDto request = CourseRequestDto.builder()
                .name("Test Course " + System.currentTimeMillis())
                .instructor("Test Instructor")
                .durationHours(20)
                .cost(new BigDecimal("29.99"))
                .category("Testing")
                .build();

        mockMvc.perform(post("/api/v1/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value(request.getName()))
                .andExpect(jsonPath("$.data.instructor").value("Test Instructor"))
                .andExpect(jsonPath("$.data.cost").value(29.99));
    }

    @Test
    @DisplayName("POST /api/v1/courses - should return 400 for missing required fields")
    void shouldReturn400ForInvalidCourse() throws Exception {
        CourseRequestDto invalid = CourseRequestDto.builder().build();

        mockMvc.perform(post("/api/v1/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("GET /api/v1/courses/{id} - should return 404 for nonexistent course")
    void shouldReturn404ForMissingCourse() throws Exception {
        mockMvc.perform(get("/api/v1/courses/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("GET /api/v1/courses/categories - should return list of categories")
    void shouldReturnCategories() throws Exception {
        mockMvc.perform(get("/api/v1/courses/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
}
