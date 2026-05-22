package com.eduplatform.repository;

import com.eduplatform.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);

    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.status = :status")
    List<Enrollment> findByStudentIdAndStatus(
        @Param("studentId") Long studentId,
        @Param("status") Enrollment.EnrollmentStatus status
    );

    @Query("SELECT COUNT(e) FROM Enrollment e JOIN e.courses c WHERE c.id = :courseId")
    Long countEnrollmentsByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT e FROM Enrollment e JOIN e.courses c WHERE c.id = :courseId")
    List<Enrollment> findByCourseId(@Param("courseId") Long courseId);
}
