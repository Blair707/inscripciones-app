package com.eduplatform.controller;

import com.eduplatform.dto.ApiResponseDto;
import com.eduplatform.dto.EnrollmentRequestDto;
import com.eduplatform.dto.EnrollmentSummaryDto;
import com.eduplatform.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
@Tag(name = "Enrollments", description = "Course enrollment endpoints")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @Operation(
        summary = "Enroll student in courses",
        description = "Enrolls a student in one or more courses. Returns full summary with costs, " +
                      "discounts (5% for 2 courses, 10% for 3+), and total amount to pay."
    )
    public ResponseEntity<ApiResponseDto<EnrollmentSummaryDto>> enrollStudent(
            @Valid @RequestBody EnrollmentRequestDto request) {
        EnrollmentSummaryDto summary = enrollmentService.enrollStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(summary, "Enrollment completed successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get enrollment details by ID")
    public ResponseEntity<ApiResponseDto<EnrollmentSummaryDto>> getEnrollmentById(
            @PathVariable Long id) {
        EnrollmentSummaryDto summary = enrollmentService.getEnrollmentById(id);
        return ResponseEntity.ok(ApiResponseDto.success(summary, "Enrollment found"));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get all enrollments for a student")
    public ResponseEntity<ApiResponseDto<List<EnrollmentSummaryDto>>> getEnrollmentsByStudent(
            @PathVariable Long studentId) {
        List<EnrollmentSummaryDto> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        return ResponseEntity.ok(ApiResponseDto.success(enrollments,
                "Found " + enrollments.size() + " enrollments for student"));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel an enrollment")
    public ResponseEntity<ApiResponseDto<EnrollmentSummaryDto>> cancelEnrollment(
            @PathVariable Long id) {
        EnrollmentSummaryDto summary = enrollmentService.cancelEnrollment(id);
        return ResponseEntity.ok(ApiResponseDto.success(summary, "Enrollment cancelled successfully"));
    }
}
