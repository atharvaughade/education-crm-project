package com.atharva.educationcrm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.atharva.educationcrm.dto.CheckoutForm;
import com.atharva.educationcrm.entity.Course;
import com.atharva.educationcrm.entity.Course.CourseLevel;
import com.atharva.educationcrm.entity.Course.CourseStatus;
import com.atharva.educationcrm.entity.Payment.PaymentMethod;
import com.atharva.educationcrm.entity.Payment.PaymentStatus;
import com.atharva.educationcrm.entity.Student;
import com.atharva.educationcrm.exception.DuplicateResourceException;
import com.atharva.educationcrm.repository.CourseRepository;
import com.atharva.educationcrm.repository.StudentRepository;
import com.atharva.educationcrm.service.EnrollmentService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EnrollmentServiceIntegrationTests {

    @Autowired private EnrollmentService enrollmentService;
    @Autowired private StudentRepository studentRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setFirstName("Enrollment");
        student.setLastName("Student");
        student.setEmail("enrollment@test.com");
        student.setMobile("9765432109");
        student.setCity("Mumbai");
        student.setCourse("Not Assigned");
        student.setAdmissionDate(LocalDate.now());
        student.setRegistrationType("SELF");
        student.setStatus("ACTIVE");
        student.setPassword(passwordEncoder.encode("Password@123"));
        student = studentRepository.save(student);

        course = new Course();
        course.setTitle("Spring Security Interview Lab");
        course.setCategory("Java");
        course.setDescription("A practical security course for integration testing.");
        course.setPrice(new BigDecimal("1999.00"));
        course.setDuration(24);
        course.setLevel(CourseLevel.INTERMEDIATE);
        course.setStatus(CourseStatus.ACTIVE);
        course = courseRepository.save(course);
    }

    @Test
    void checkoutCreatesPaymentAndEnrollment() {
        CheckoutForm form = new CheckoutForm();
        form.setCourseId(course.getId());
        form.setPaymentMethod(PaymentMethod.UPI);
        form.setConfirmed(true);

        var payment = enrollmentService.checkout(student.getEmail(), form);

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(payment.getReference()).startsWith("PAY-");
        assertThat(enrollmentService.getStudentEnrollments(student.getEmail())).hasSize(1);
    }

    @Test
    void duplicateEnrollmentIsRejected() {
        CheckoutForm form = new CheckoutForm();
        form.setCourseId(course.getId());
        form.setPaymentMethod(PaymentMethod.CARD);
        form.setConfirmed(true);
        enrollmentService.checkout(student.getEmail(), form);

        assertThatThrownBy(() -> enrollmentService.checkout(student.getEmail(), form))
                .isInstanceOf(DuplicateResourceException.class);
    }
}
