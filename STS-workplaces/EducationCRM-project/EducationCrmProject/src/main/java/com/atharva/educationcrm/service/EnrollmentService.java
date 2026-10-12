package com.atharva.educationcrm.service;

import java.util.List;

import com.atharva.educationcrm.dto.CheckoutForm;
import com.atharva.educationcrm.entity.Enrollment;
import com.atharva.educationcrm.entity.Payment;

public interface EnrollmentService {
    Payment checkout(String studentEmail, CheckoutForm form);
    List<Enrollment> getStudentEnrollments(String studentEmail);
    List<Enrollment> getAllEnrollments();
    List<Payment> getAllPayments();
    List<Payment> getStudentPayments(String studentEmail);
    boolean isEnrolled(String studentEmail, Long courseId);
    Enrollment updateProgress(String studentEmail, Long enrollmentId, int progress);
}
