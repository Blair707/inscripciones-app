package com.eduplatform.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ENROLLMENTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STUDENT_ID", nullable = false)
    private Student student;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "ENROLLMENT_COURSES",
        joinColumns = @JoinColumn(name = "ENROLLMENT_ID"),
        inverseJoinColumns = @JoinColumn(name = "COURSE_ID")
    )
    private List<Course> courses;

    @Column(name = "TOTAL_AMOUNT", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "DISCOUNT_PERCENTAGE", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal discountPercentage = BigDecimal.ZERO;

    @Column(name = "FINAL_AMOUNT", nullable = false, precision = 10, scale = 2)
    private BigDecimal finalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_METHOD", length = 30)
    private PaymentMethod paymentMethod;

    @Column(name = "ENROLLMENT_DATE", nullable = false, updatable = false)
    private LocalDateTime enrollmentDate;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        enrollmentDate = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum EnrollmentStatus {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }

    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, PAYPAL, CASH
    }
}
