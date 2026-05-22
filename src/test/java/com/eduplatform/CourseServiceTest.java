package com.eduplatform.service;

import com.eduplatform.dto.CourseRequestDto;
import com.eduplatform.dto.CourseResponseDto;
import com.eduplatform.exception.BusinessException;
import com.eduplatform.exception.ResourceNotFoundException;
import com.eduplatform.model.Course;
import com.eduplatform.repository.CourseRepository;
import com.eduplatform.repository.EnrollmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseService Unit Tests")
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private CourseService courseService;

    private Course sampleCourse;
    private CourseRequestDto sampleRequest;

    @BeforeEach
    void setUp() {
        sampleCourse = Course.builder()
                .id(1L)
                .name("Java Spring Boot")
                .instructor("Carlos Ramírez")
                .durationHours(40)
                .cost(new BigDecimal("49.99"))
                .category("Backend")
                .available(true)
                .build();

        sampleRequest = CourseRequestDto.builder()
                .name("Java Spring Boot")
                .instructor("Carlos Ramírez")
                .durationHours(40)
                .cost(new BigDecimal("49.99"))
                .category("Backend")
                .build();
    }

    @Test
    @DisplayName("Should return all available courses")
    void shouldReturnAllAvailableCourses() {
        when(courseRepository.findByAvailableTrue()).thenReturn(List.of(sampleCourse));
        when(enrollmentRepository.countEnrollmentsByCourseId(anyLong())).thenReturn(0L);

        List<CourseResponseDto> result = courseService.getAllAvailableCourses();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Java Spring Boot");
        assertThat(result.get(0).getInstructor()).isEqualTo("Carlos Ramírez");
        verify(courseRepository).findByAvailableTrue();
    }

    @Test
    @DisplayName("Should create a course successfully")
    void shouldCreateCourseSuccessfully() {
        when(courseRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(sampleCourse);
        when(enrollmentRepository.countEnrollmentsByCourseId(anyLong())).thenReturn(0L);

        CourseResponseDto result = courseService.createCourse(sampleRequest);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Java Spring Boot");
        assertThat(result.getCost()).isEqualByComparingTo(new BigDecimal("49.99"));
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when course name already exists")
    void shouldThrowExceptionWhenCourseNameExists() {
        when(courseRepository.existsByNameIgnoreCase(anyString())).thenReturn(true);

        assertThatThrownBy(() -> courseService.createCourse(sampleRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already exists");

        verify(courseRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when course not found")
    void shouldThrowNotFoundWhenCourseNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.getCourseById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Should toggle course availability")
    void shouldToggleCourseAvailability() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(sampleCourse));
        when(courseRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(enrollmentRepository.countEnrollmentsByCourseId(anyLong())).thenReturn(0L);

        CourseResponseDto result = courseService.toggleCourseAvailability(1L);

        assertThat(result.getAvailable()).isFalse();
    }
}
