package com.eduplatform.service;

import com.eduplatform.dto.EnrollmentRequestDto;
import com.eduplatform.dto.EnrollmentSummaryDto;
import com.eduplatform.exception.BusinessException;
import com.eduplatform.exception.ResourceNotFoundException;
import com.eduplatform.model.Course;
import com.eduplatform.model.Enrollment;
import com.eduplatform.model.Student;
import com.eduplatform.repository.CourseRepository;
import com.eduplatform.repository.EnrollmentRepository;
import com.eduplatform.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    // Discount rules: 2 courses = 5%, 3+ courses = 10%
    private static final BigDecimal DISCOUNT_2_COURSES = new BigDecimal("5.00");
    private static final BigDecimal DISCOUNT_3_PLUS_COURSES = new BigDecimal("10.00");

    @Transactional
    public EnrollmentSummaryDto enrollStudent(EnrollmentRequestDto request) {
        log.info("Processing enrollment for student ID: {}", request.getStudentId());

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", request.getStudentId()));

        List<Long> courseIds = request.getCourseIds().stream().distinct().collect(Collectors.toList());
        if (courseIds.isEmpty()) {
            throw new BusinessException("At least one course must be selected");
        }

        List<Course> courses = courseIds.stream()
                .map(id -> courseRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Course", id)))
                .collect(Collectors.toList());

        // Validate all courses are available
        courses.forEach(course -> {
            if (!course.getAvailable()) {
                throw new BusinessException("Course '" + course.getName() + "' is not currently available");
            }
            // Check capacity
            if (course.getMaxStudents() != null) {
                Long enrolled = enrollmentRepository.countEnrollmentsByCourseId(course.getId());
                if (enrolled >= course.getMaxStudents()) {
                    throw new BusinessException("Course '" + course.getName() + "' has reached maximum capacity");
                }
            }
        });

        // Calculate amounts
        BigDecimal subtotal = courses.stream()
                .map(Course::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discountPct = calculateDiscount(courses.size());
        BigDecimal discountAmount = subtotal.multiply(discountPct)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal finalAmount = subtotal.subtract(discountAmount);

        // Resolve payment method
        Enrollment.PaymentMethod paymentMethod = null;
        if (request.getPaymentMethod() != null) {
            try {
                paymentMethod = Enrollment.PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("Invalid payment method: " + request.getPaymentMethod());
            }
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .courses(courses)
                .totalAmount(subtotal)
                .discountPercentage(discountPct)
                .finalAmount(finalAmount)
                .status(Enrollment.EnrollmentStatus.CONFIRMED)
                .paymentMethod(paymentMethod)
                .build();

        Enrollment saved = enrollmentRepository.save(enrollment);
        log.info("Enrollment created with ID: {}", saved.getId());
        return mapToSummaryDto(saved);
    }

    @Transactional(readOnly = true)
    public EnrollmentSummaryDto getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", id));
        return mapToSummaryDto(enrollment);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentSummaryDto> getEnrollmentsByStudent(Long studentId) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", studentId));
        return enrollmentRepository.findByStudentId(studentId)
                .stream()
                .map(this::mapToSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EnrollmentSummaryDto cancelEnrollment(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", id));
        if (enrollment.getStatus() == Enrollment.EnrollmentStatus.CANCELLED) {
            throw new BusinessException("Enrollment is already cancelled");
        }
        enrollment.setStatus(Enrollment.EnrollmentStatus.CANCELLED);
        return mapToSummaryDto(enrollmentRepository.save(enrollment));
    }

    private BigDecimal calculateDiscount(int courseCount) {
        if (courseCount >= 3) return DISCOUNT_3_PLUS_COURSES;
        if (courseCount == 2) return DISCOUNT_2_COURSES;
        return BigDecimal.ZERO;
    }

    private EnrollmentSummaryDto mapToSummaryDto(Enrollment enrollment) {
        List<EnrollmentSummaryDto.EnrollmentCourseItemDto> courseItems = enrollment.getCourses()
                .stream()
                .map(c -> EnrollmentSummaryDto.EnrollmentCourseItemDto.builder()
                        .courseId(c.getId())
                        .courseName(c.getName())
                        .instructor(c.getInstructor())
                        .durationHours(c.getDurationHours())
                        .cost(c.getCost())
                        .category(c.getCategory())
                        .build())
                .collect(Collectors.toList());

        BigDecimal discountAmount = enrollment.getTotalAmount()
                .multiply(enrollment.getDiscountPercentage())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        return EnrollmentSummaryDto.builder()
                .enrollmentId(enrollment.getId())
                .studentId(enrollment.getStudent().getId())
                .studentName(enrollment.getStudent().getName())
                .studentEmail(enrollment.getStudent().getEmail())
                .courses(courseItems)
                .subtotal(enrollment.getTotalAmount())
                .discountPercentage(enrollment.getDiscountPercentage())
                .discountAmount(discountAmount)
                .totalAmount(enrollment.getFinalAmount())
                .paymentMethod(enrollment.getPaymentMethod() != null
                        ? enrollment.getPaymentMethod().name() : null)
                .status(enrollment.getStatus().name())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .build();
    }
}
