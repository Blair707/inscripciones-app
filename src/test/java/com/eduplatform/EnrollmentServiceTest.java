package com.eduplatform.service;

import com.eduplatform.dto.EnrollmentRequestDto;
import com.eduplatform.dto.EnrollmentSummaryDto;
import com.eduplatform.exception.BusinessException;
import com.eduplatform.model.Course;
import com.eduplatform.model.Enrollment;
import com.eduplatform.model.Student;
import com.eduplatform.repository.CourseRepository;
import com.eduplatform.repository.EnrollmentRepository;
import com.eduplatform.repository.StudentRepository;
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
@DisplayName("EnrollmentService Unit Tests")
class EnrollmentServiceTest {

    @Mock private EnrollmentRepository enrollmentRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private CourseRepository courseRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Student student;
    private Course course1, course2, course3;

    @BeforeEach
    void setUp() {
        student = Student.builder().id(1L).name("Juan Pérez").email("juan@test.com").build();

        course1 = Course.builder().id(1L).name("Java").instructor("Carlos")
                .durationHours(40).cost(new BigDecimal("50.00")).category("Backend").available(true).build();
        course2 = Course.builder().id(2L).name("React").instructor("María")
                .durationHours(35).cost(new BigDecimal("60.00")).category("Frontend").available(true).build();
        course3 = Course.builder().id(3L).name("Python").instructor("Andrés")
                .durationHours(50).cost(new BigDecimal("70.00")).category("Data Science").available(true).build();
    }

    @Test
    @DisplayName("Should enroll student in single course with no discount")
    void shouldEnrollInSingleCourseWithNoDiscount() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course1));
        when(enrollmentRepository.countEnrollmentsByCourseId(anyLong())).thenReturn(0L);
        when(enrollmentRepository.save(any())).thenAnswer(inv -> {
            Enrollment e = inv.getArgument(0);
            e.setId(1L);
            return e;
        });

        EnrollmentRequestDto request = new EnrollmentRequestDto(1L, List.of(1L), "CREDIT_CARD");
        EnrollmentSummaryDto result = enrollmentService.enrollStudent(request);

        assertThat(result.getSubtotal()).isEqualByComparingTo("50.00");
        assertThat(result.getDiscountPercentage()).isEqualByComparingTo("0.00");
        assertThat(result.getTotalAmount()).isEqualByComparingTo("50.00");
    }

    @Test
    @DisplayName("Should apply 5% discount for 2 courses")
    void shouldApply5PercentDiscountFor2Courses() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course1));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course2));
        when(enrollmentRepository.countEnrollmentsByCourseId(anyLong())).thenReturn(0L);
        when(enrollmentRepository.save(any())).thenAnswer(inv -> {
            Enrollment e = inv.getArgument(0);
            e.setId(1L);
            return e;
        });

        EnrollmentRequestDto request = new EnrollmentRequestDto(1L, List.of(1L, 2L), null);
        EnrollmentSummaryDto result = enrollmentService.enrollStudent(request);

        // Subtotal = 110.00, 5% discount = 5.50, total = 104.50
        assertThat(result.getSubtotal()).isEqualByComparingTo("110.00");
        assertThat(result.getDiscountPercentage()).isEqualByComparingTo("5.00");
        assertThat(result.getTotalAmount()).isEqualByComparingTo("104.50");
    }

    @Test
    @DisplayName("Should apply 10% discount for 3 or more courses")
    void shouldApply10PercentDiscountFor3Courses() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course1));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course2));
        when(courseRepository.findById(3L)).thenReturn(Optional.of(course3));
        when(enrollmentRepository.countEnrollmentsByCourseId(anyLong())).thenReturn(0L);
        when(enrollmentRepository.save(any())).thenAnswer(inv -> {
            Enrollment e = inv.getArgument(0);
            e.setId(1L);
            return e;
        });

        EnrollmentRequestDto request = new EnrollmentRequestDto(1L, List.of(1L, 2L, 3L), null);
        EnrollmentSummaryDto result = enrollmentService.enrollStudent(request);

        // Subtotal = 180.00, 10% discount = 18.00, total = 162.00
        assertThat(result.getSubtotal()).isEqualByComparingTo("180.00");
        assertThat(result.getDiscountPercentage()).isEqualByComparingTo("10.00");
        assertThat(result.getTotalAmount()).isEqualByComparingTo("162.00");
    }

    @Test
    @DisplayName("Should throw exception for unavailable course")
    void shouldThrowExceptionForUnavailableCourse() {
        course1.setAvailable(false);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course1));
        when(enrollmentRepository.countEnrollmentsByCourseId(anyLong())).thenReturn(0L);

        EnrollmentRequestDto request = new EnrollmentRequestDto(1L, List.of(1L), null);

        assertThatThrownBy(() -> enrollmentService.enrollStudent(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("not currently available");
    }

    @Test
    @DisplayName("Should throw exception for full course")
    void shouldThrowExceptionForFullCourse() {
        course1.setMaxStudents(10);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course1));
        when(enrollmentRepository.countEnrollmentsByCourseId(1L)).thenReturn(10L);

        EnrollmentRequestDto request = new EnrollmentRequestDto(1L, List.of(1L), null);

        assertThatThrownBy(() -> enrollmentService.enrollStudent(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("maximum capacity");
    }
}
