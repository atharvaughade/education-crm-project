package com.atharva.educationcrm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.atharva.educationcrm.entity.Enrollment;
import com.atharva.educationcrm.entity.Enrollment.EnrollmentStatus;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @EntityGraph(attributePaths = {"course", "student"})
    List<Enrollment> findByStudentEmailIgnoreCaseOrderByEnrolledAtDesc(String email);

    @EntityGraph(attributePaths = {"course", "student"})
    List<Enrollment> findAllByOrderByEnrolledAtDesc();

    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);
    Optional<Enrollment> findByIdAndStudentEmailIgnoreCase(Long id, String email);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    long countByStudentEmailIgnoreCaseAndStatus(String email, EnrollmentStatus status);
}
