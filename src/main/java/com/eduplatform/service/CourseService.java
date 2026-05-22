package com.eduplatform.service;

import com.eduplatform.dto.CourseRequestDto;
import com.eduplatform.dto.CourseResponseDto;
import com.eduplatform.exception.BusinessException;
import com.eduplatform.exception.ResourceNotFoundException;
import com.eduplatform.model.Course;
import com.eduplatform.repository.CourseRepository;
import com.eduplatform.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional(readOnly = true)
    public List<CourseResponseDto> getAllAvailableCourses() {
        log.info("Fetching all available courses");
        return courseRepository.findByAvailableTrue()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseResponseDto> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CourseResponseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
        return mapToResponseDto(course);
    }

    @Transactional
    public CourseResponseDto createCourse(CourseRequestDto request) {
        log.info("Creating new course: {}", request.getName());
        if (courseRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BusinessException("A course with the name '" + request.getName() + "' already exists");
        }
        Course course = Course.builder()
                .name(request.getName())
                .instructor(request.getInstructor())
                .durationHours(request.getDurationHours())
                .cost(request.getCost())
                .description(request.getDescription())
                .category(request.getCategory())
                .maxStudents(request.getMaxStudents())
                .available(true)
                .build();

        Course saved = courseRepository.save(course);
        log.info("Course created successfully with ID: {}", saved.getId());
        return mapToResponseDto(saved);
    }

    @Transactional
    public CourseResponseDto updateCourse(Long id, CourseRequestDto request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));

        course.setName(request.getName());
        course.setInstructor(request.getInstructor());
        course.setDurationHours(request.getDurationHours());
        course.setCost(request.getCost());
        course.setDescription(request.getDescription());
        course.setCategory(request.getCategory());
        course.setMaxStudents(request.getMaxStudents());

        return mapToResponseDto(courseRepository.save(course));
    }

    @Transactional
    public CourseResponseDto toggleCourseAvailability(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
        course.setAvailable(!course.getAvailable());
        return mapToResponseDto(courseRepository.save(course));
    }

    @Transactional(readOnly = true)
    public List<CourseResponseDto> getCoursesByCategory(String category) {
        return courseRepository.findByCategoryAndAvailableTrue(category)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseResponseDto> searchCourses(String keyword) {
        return courseRepository.searchByKeyword(keyword)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseResponseDto> getCoursesByPriceRange(BigDecimal min, BigDecimal max) {
        return courseRepository.findAvailableCoursesByCostRange(min, max)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        return courseRepository.findAllCategories();
    }

    private CourseResponseDto mapToResponseDto(Course course) {
        Long enrolledCount = enrollmentRepository.countEnrollmentsByCourseId(course.getId());
        return CourseResponseDto.builder()
                .id(course.getId())
                .name(course.getName())
                .instructor(course.getInstructor())
                .durationHours(course.getDurationHours())
                .cost(course.getCost())
                .description(course.getDescription())
                .category(course.getCategory())
                .available(course.getAvailable())
                .maxStudents(course.getMaxStudents())
                .enrolledCount(enrolledCount)
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}
