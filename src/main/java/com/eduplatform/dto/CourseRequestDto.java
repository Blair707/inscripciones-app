package com.eduplatform.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequestDto {

    @NotBlank(message = "Course name is required")
    @Size(max = 200, message = "Course name must not exceed 200 characters")
    private String name;

    @NotBlank(message = "Instructor name is required")
    @Size(max = 100)
    private String instructor;

    @NotNull(message = "Duration (hours) is required")
    @Positive(message = "Duration must be positive")
    private Integer durationHours;

    @NotNull(message = "Cost is required")
    @DecimalMin(value = "0.0", message = "Cost must be non-negative")
    private BigDecimal cost;

    @Size(max = 1000)
    private String description;

    @NotBlank(message = "Category is required")
    @Size(max = 100)
    private String category;

    @Positive(message = "Max students must be positive")
    private Integer maxStudents;
}
