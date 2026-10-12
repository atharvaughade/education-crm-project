package com.atharva.educationcrm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.atharva.educationcrm.entity.Course;
import com.atharva.educationcrm.entity.Course.CourseStatus;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String title, String category);
    List<Course> findByStatusOrderByCreatedAtDesc(CourseStatus status);
    long countByStatus(CourseStatus status);
}
