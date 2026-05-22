package com.eduplatform.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentRequestDto {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Course list is required")
    @NotEmpty(message = "At least one course must be selected")
    private List<Long> courseIds;

    private String paymentMethod;
}
