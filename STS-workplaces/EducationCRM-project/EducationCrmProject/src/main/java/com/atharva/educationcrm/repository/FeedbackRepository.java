package com.atharva.educationcrm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.atharva.educationcrm.entity.Feedback;
import com.atharva.educationcrm.entity.Feedback.FeedbackStatus;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    @EntityGraph(attributePaths = "student")
    List<Feedback> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = "student")
    List<Feedback> findByStudentEmailIgnoreCaseOrderByCreatedAtDesc(String email);

    long countByStatus(FeedbackStatus status);
}
