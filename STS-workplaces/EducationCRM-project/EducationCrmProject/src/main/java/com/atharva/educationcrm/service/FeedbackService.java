package com.atharva.educationcrm.service;

import java.util.List;

import com.atharva.educationcrm.entity.Feedback;
import com.atharva.educationcrm.entity.Feedback.FeedbackStatus;

public interface FeedbackService {
    Feedback create(String studentEmail, Feedback feedback);
    List<Feedback> getForStudent(String studentEmail);
    List<Feedback> getAll();
    Feedback respond(Long id, FeedbackStatus status, String response);
}
