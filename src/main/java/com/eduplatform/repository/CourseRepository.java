package com.eduplatform.repository;

import com.eduplatform.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByAvailableTrue();

    List<Course> findByCategoryAndAvailableTrue(String category);

    List<Course> findByInstructorContainingIgnoreCase(String instructor);

    @Query("SELECT c FROM Course c WHERE c.available = true AND c.cost BETWEEN :minCost AND :maxCost")
    List<Course> findAvailableCoursesByCostRange(
        @Param("minCost") BigDecimal minCost,
        @Param("maxCost") BigDecimal maxCost
    );

    @Query("SELECT c FROM Course c WHERE c.available = true AND LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Course> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT c.category FROM Course c WHERE c.available = true")
    List<String> findAllCategories();

    boolean existsByNameIgnoreCase(String name);
}
