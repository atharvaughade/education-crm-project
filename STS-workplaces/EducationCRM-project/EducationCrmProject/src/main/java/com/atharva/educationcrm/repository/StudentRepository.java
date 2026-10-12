package com.atharva.educationcrm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.atharva.educationcrm.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByMobile(String mobile);
    long countByStatusIgnoreCase(String status);

    List<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrMobileContainingIgnoreCaseOrCourseContainingIgnoreCase(
            String firstName, String lastName, String email, String mobile, String course);
}
