package com.eduplatform.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentSummaryDto {
    private Long enrollmentId;
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private List<EnrollmentCourseItemDto> courses;
    private BigDecimal subtotal;
    private BigDecimal discountPercentage;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String status;
    private LocalDateTime enrollmentDate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EnrollmentCourseItemDto {
        private Long courseId;
        private String courseName;
        private String instructor;
        private Integer durationHours;
        private BigDecimal cost;
        private String category;
    }
}
