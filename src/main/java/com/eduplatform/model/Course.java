package com.eduplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "COURSES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank(message = "Course name is required")
    @Size(max = 200, message = "Course name must not exceed 200 characters")
    @Column(name = "NAME", nullable = false, length = 200)
    private String name;

    @NotBlank(message = "Instructor name is required")
    @Size(max = 100, message = "Instructor name must not exceed 100 characters")
    @Column(name = "INSTRUCTOR", nullable = false, length = 100)
    private String instructor;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be a positive number")
    @Column(name = "DURATION_HOURS", nullable = false)
    private Integer durationHours;

    @NotNull(message = "Cost is required")
    @DecimalMin(value = "0.0", message = "Cost must be non-negative")
    @Digits(integer = 8, fraction = 2, message = "Cost format is invalid")
    @Column(name = "COST", nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    @NotBlank(message = "Category is required")
    @Size(max = 100)
    @Column(name = "CATEGORY", length = 100)
    private String category;

    @Column(name = "AVAILABLE", nullable = false)
    @Builder.Default
    private Boolean available = true;

    @Column(name = "MAX_STUDENTS")
    private Integer maxStudents;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
