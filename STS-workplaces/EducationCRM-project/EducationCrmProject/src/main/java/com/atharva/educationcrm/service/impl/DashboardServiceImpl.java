package com.atharva.educationcrm.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atharva.educationcrm.dto.AdminDashboardStats;
import com.atharva.educationcrm.entity.Course.CourseStatus;
import com.atharva.educationcrm.entity.Feedback.FeedbackStatus;
import com.atharva.educationcrm.entity.Inquiry.InquiryStatus;
import com.atharva.educationcrm.entity.Payment.PaymentStatus;
import com.atharva.educationcrm.repository.CourseRepository;
import com.atharva.educationcrm.repository.EmployeeRepository;
import com.atharva.educationcrm.repository.EnrollmentRepository;
import com.atharva.educationcrm.repository.FeedbackRepository;
import com.atharva.educationcrm.repository.InquiryRepository;
import com.atharva.educationcrm.repository.PaymentRepository;
import com.atharva.educationcrm.repository.StudentRepository;
import com.atharva.educationcrm.service.DashboardService;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EmployeeRepository employeeRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final InquiryRepository inquiryRepository;
    private final FeedbackRepository feedbackRepository;
    private final PaymentRepository paymentRepository;

    public DashboardServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository,
            EmployeeRepository employeeRepository, EnrollmentRepository enrollmentRepository,
            InquiryRepository inquiryRepository, FeedbackRepository feedbackRepository,
            PaymentRepository paymentRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.employeeRepository = employeeRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.inquiryRepository = inquiryRepository;
        this.feedbackRepository = feedbackRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public AdminDashboardStats getAdminStats() {
        return new AdminDashboardStats(
                studentRepository.countByStatusIgnoreCase("ACTIVE"),
                courseRepository.countByStatus(CourseStatus.ACTIVE),
                employeeRepository.count(),
                enrollmentRepository.count(),
                inquiryRepository.countByStatus(InquiryStatus.NEW),
                feedbackRepository.countByStatus(FeedbackStatus.OPEN),
                paymentRepository.sumAmountByStatus(PaymentStatus.COMPLETED));
    }
}
