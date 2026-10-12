package com.atharva.educationcrm.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atharva.educationcrm.dto.CheckoutForm;
import com.atharva.educationcrm.entity.Course;
import com.atharva.educationcrm.entity.Course.CourseStatus;
import com.atharva.educationcrm.entity.Enrollment;
import com.atharva.educationcrm.entity.Enrollment.EnrollmentStatus;
import com.atharva.educationcrm.entity.Payment;
import com.atharva.educationcrm.entity.Payment.PaymentStatus;
import com.atharva.educationcrm.entity.Student;
import com.atharva.educationcrm.exception.DuplicateResourceException;
import com.atharva.educationcrm.exception.ResourceNotFoundException;
import com.atharva.educationcrm.repository.CourseRepository;
import com.atharva.educationcrm.repository.EnrollmentRepository;
import com.atharva.educationcrm.repository.PaymentRepository;
import com.atharva.educationcrm.repository.StudentRepository;
import com.atharva.educationcrm.service.EnrollmentService;

@Service
@Transactional(readOnly = true)
public class EnrollmentServiceImpl implements EnrollmentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentRepository paymentRepository;

    public EnrollmentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository,
            EnrollmentRepository enrollmentRepository, PaymentRepository paymentRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public Payment checkout(String studentEmail, CheckoutForm form) {
        Student student = studentRepository.findByEmailIgnoreCase(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Student account not found"));
        
        Course course = courseRepository.findById(form.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        
        if (course.getStatus() != CourseStatus.ACTIVE) {
            throw new IllegalStateException("This course is not accepting enrollments");
        }
        
        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            throw new DuplicateResourceException("You are already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setProgress(0);
        enrollment = enrollmentRepository.save(enrollment);

        Payment payment = new Payment();
        payment.setStudent(student);
        payment.setCourse(course);
        payment.setEnrollment(enrollment);
        payment.setAmount(course.getPrice());
        payment.setMethod(form.getPaymentMethod());
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaidAt(LocalDateTime.now());
        payment.setReference("PAY-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
        return paymentRepository.save(payment);
    }

    @Override
    public List<Enrollment> getStudentEnrollments(String studentEmail) {
        return enrollmentRepository.findByStudentEmailIgnoreCaseOrderByEnrolledAtDesc(studentEmail);
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAllByOrderByEnrolledAtDesc();
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<Payment> getStudentPayments(String studentEmail) {
        return paymentRepository.findByStudentEmailIgnoreCaseOrderByCreatedAtDesc(studentEmail);
    }

    @Override
    public boolean isEnrolled(String studentEmail, Long courseId) {
        return studentRepository.findByEmailIgnoreCase(studentEmail)
                .map(student -> enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId))
                .orElse(false);
    }

    @Override
    @Transactional
    public Enrollment updateProgress(String studentEmail, Long enrollmentId, int progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }
        Enrollment enrollment = enrollmentRepository.findByIdAndStudentEmailIgnoreCase(enrollmentId, studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        
        enrollment.setProgress(progress);
        enrollment.setStatus(progress == 100 ? EnrollmentStatus.COMPLETED : EnrollmentStatus.ACTIVE);
        return enrollmentRepository.save(enrollment);
    }
}
