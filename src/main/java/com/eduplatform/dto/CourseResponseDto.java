package com.eduplatform.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponseDto {
    private Long id;
    private String name;
    private String instructor;
    private Integer durationHours;
    private BigDecimal cost;
    private String description;
    private String category;
    private Boolean available;
    private Integer maxStudents;
    private Long enrolledCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
