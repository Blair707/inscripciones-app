package com.eduplatform.controller;

import com.eduplatform.dto.ApiResponseDto;
import com.eduplatform.dto.CourseRequestDto;
import com.eduplatform.dto.CourseResponseDto;
import com.eduplatform.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Course management endpoints")
@CrossOrigin(origins = "*")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    @Operation(summary = "Get all available courses",
               description = "Returns a list of all available courses with name, instructor, duration and cost")
    public ResponseEntity<ApiResponseDto<List<CourseResponseDto>>> getAvailableCourses() {
        List<CourseResponseDto> courses = courseService.getAllAvailableCourses();
        return ResponseEntity.ok(ApiResponseDto.success(courses,
                "Found " + courses.size() + " available courses"));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all courses (including unavailable)")
    public ResponseEntity<ApiResponseDto<List<CourseResponseDto>>> getAllCourses() {
        List<CourseResponseDto> courses = courseService.getAllCourses();
        return ResponseEntity.ok(ApiResponseDto.success(courses,
                "Found " + courses.size() + " courses"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID")
    public ResponseEntity<ApiResponseDto<CourseResponseDto>> getCourseById(@PathVariable Long id) {
        CourseResponseDto course = courseService.getCourseById(id);
        return ResponseEntity.ok(ApiResponseDto.success(course, "Course found"));
    }

    @PostMapping
    @Operation(summary = "Add new course", description = "Creates a new course in the educational platform")
    public ResponseEntity<ApiResponseDto<CourseResponseDto>> createCourse(
            @Valid @RequestBody CourseRequestDto request) {
        CourseResponseDto created = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(created, "Course created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing course")
    public ResponseEntity<ApiResponseDto<CourseResponseDto>> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequestDto request) {
        CourseResponseDto updated = courseService.updateCourse(id, request);
        return ResponseEntity.ok(ApiResponseDto.success(updated, "Course updated successfully"));
    }

    @PatchMapping("/{id}/toggle-availability")
    @Operation(summary = "Toggle course availability")
    public ResponseEntity<ApiResponseDto<CourseResponseDto>> toggleAvailability(@PathVariable Long id) {
        CourseResponseDto updated = courseService.toggleCourseAvailability(id);
        return ResponseEntity.ok(ApiResponseDto.success(updated,
                "Course availability updated to: " + updated.getAvailable()));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get courses by category")
    public ResponseEntity<ApiResponseDto<List<CourseResponseDto>>> getCoursesByCategory(
            @PathVariable String category) {
        List<CourseResponseDto> courses = courseService.getCoursesByCategory(category);
        return ResponseEntity.ok(ApiResponseDto.success(courses,
                "Found " + courses.size() + " courses in category: " + category));
    }

    @GetMapping("/search")
    @Operation(summary = "Search courses by keyword")
    public ResponseEntity<ApiResponseDto<List<CourseResponseDto>>> searchCourses(
            @RequestParam String keyword) {
        List<CourseResponseDto> courses = courseService.searchCourses(keyword);
        return ResponseEntity.ok(ApiResponseDto.success(courses,
                "Found " + courses.size() + " courses matching: " + keyword));
    }

    @GetMapping("/filter/price")
    @Operation(summary = "Filter courses by price range")
    public ResponseEntity<ApiResponseDto<List<CourseResponseDto>>> getCoursesByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        List<CourseResponseDto> courses = courseService.getCoursesByPriceRange(min, max);
        return ResponseEntity.ok(ApiResponseDto.success(courses,
                "Found " + courses.size() + " courses in price range"));
    }

    @GetMapping("/categories")
    @Operation(summary = "Get all available categories")
    public ResponseEntity<ApiResponseDto<List<String>>> getAllCategories() {
        List<String> categories = courseService.getAllCategories();
        return ResponseEntity.ok(ApiResponseDto.success(categories,
                "Found " + categories.size() + " categories"));
    }
}
