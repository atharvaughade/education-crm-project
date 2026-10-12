package com.atharva.educationcrm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atharva.educationcrm.entity.Feedback;
import com.atharva.educationcrm.entity.Feedback.FeedbackStatus;
import com.atharva.educationcrm.entity.Student;
import com.atharva.educationcrm.exception.ResourceNotFoundException;
import com.atharva.educationcrm.repository.FeedbackRepository;
import com.atharva.educationcrm.repository.StudentRepository;
import com.atharva.educationcrm.service.FeedbackService;

@Service
@Transactional(readOnly = true)
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final StudentRepository studentRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, StudentRepository studentRepository) {
        this.feedbackRepository = feedbackRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional
    public Feedback create(String studentEmail, Feedback feedback) {
        Student student = studentRepository.findByEmailIgnoreCase(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Student account not found"));
        feedback.setId(null);
        feedback.setStudent(student);
        feedback.setStatus(FeedbackStatus.OPEN);
        feedback.setAdminResponse(null);
        return feedbackRepository.save(feedback);
    }

    @Override
    public List<Feedback> getForStudent(String studentEmail) {
        return feedbackRepository.findByStudentEmailIgnoreCaseOrderByCreatedAtDesc(studentEmail);
    }

    @Override
    public List<Feedback> getAll() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional
    public Feedback respond(Long id, FeedbackStatus status, String response) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found"));
        feedback.setStatus(status);
        feedback.setAdminResponse(response == null ? null : response.trim());
        return feedbackRepository.save(feedback);
    }
}
