package com.eduplatform.controller;

import com.eduplatform.dto.ApiResponseDto;
import com.eduplatform.dto.StudentRequestDto;
import com.eduplatform.model.Student;
import com.eduplatform.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Tag(name = "Students", description = "Student management endpoints")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @Operation(summary = "Register a new student",
               description = "Creates a new student or returns existing one if email already registered")
    public ResponseEntity<ApiResponseDto<Student>> registerStudent(
            @Valid @RequestBody StudentRequestDto request) {
        Student student = studentService.createOrGetStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(student, "Student registered successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID")
    public ResponseEntity<ApiResponseDto<Student>> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(ApiResponseDto.success(student, "Student found"));
    }
}
