package com.atharva.educationcrm.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atharva.educationcrm.dto.PasswordChangeForm;
import com.atharva.educationcrm.dto.ProfileUpdateForm;
import com.atharva.educationcrm.dto.RegistrationForm;
import com.atharva.educationcrm.entity.Student;
import com.atharva.educationcrm.exception.DuplicateResourceException;
import com.atharva.educationcrm.exception.ResourceNotFoundException;
import com.atharva.educationcrm.repository.AdminRepository;
import com.atharva.educationcrm.repository.StudentRepository;
import com.atharva.educationcrm.service.StudentService;

@Service
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepository studentRepository, AdminRepository adminRepository,
            PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Student register(RegistrationForm form) {
        if (!form.passwordsMatch()) {
            throw new IllegalArgumentException("Password and confirmation do not match");
        }
        validateUniqueIdentity(form.getEmail(), form.getMobile(), null);
        Student student = new Student();
        student.setFirstName(form.getFirstName().trim());
        student.setLastName(form.getLastName().trim());
        student.setEmail(normalizeEmail(form.getEmail()));
        student.setMobile(form.getMobile().trim());
        student.setCity(form.getCity().trim());
        student.setPassword(passwordEncoder.encode(form.getPassword()));
        student.setAdmissionDate(LocalDate.now());
        student.setRegistrationType("SELF");
        student.setStatus("ACTIVE");
        student.setCourse("Not Assigned");
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public Student addStudent(Student student, String rawPassword) {
        if (rawPassword == null || rawPassword.length() < 8) {
            throw new IllegalArgumentException("A temporary password of at least 8 characters is required");
        }
        validateUniqueIdentity(student.getEmail(), student.getMobile(), null);
        student.setEmail(normalizeEmail(student.getEmail()));
        student.setPassword(passwordEncoder.encode(rawPassword));
        student.setRegistrationType("ADMIN");
        student.setStatus(student.getStatus() == null ? "ACTIVE" : student.getStatus());
        student.setAdmissionDate(student.getAdmissionDate() == null ? LocalDate.now() : student.getAdmissionDate());
        student.setCity(blankToDefault(student.getCity(), "Not Provided"));
        student.setCourse(blankToDefault(student.getCourse(), "Not Assigned"));
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> searchStudents(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllStudents();
        }
        String value = keyword.trim();
        return studentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrMobileContainingIgnoreCaseOrCourseContainingIgnoreCase(
                value, value, value, value, value);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }

    @Override
    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student account not found"));
    }

    @Override
    @Transactional
    public Student updateStudent(Student submitted) {
        Student existing = getStudentById(submitted.getId());
        validateUniqueIdentity(submitted.getEmail(), submitted.getMobile(), existing);
        existing.setFirstName(submitted.getFirstName().trim());
        existing.setLastName(submitted.getLastName().trim());
        existing.setEmail(normalizeEmail(submitted.getEmail()));
        existing.setMobile(submitted.getMobile().trim());
        existing.setCity(blankToDefault(submitted.getCity(), "Not Provided"));
        existing.setCourse(blankToDefault(submitted.getCourse(), "Not Assigned"));
        existing.setBatch(submitted.getBatch());
        existing.setAdmissionDate(submitted.getAdmissionDate());
        existing.setStatus(blankToDefault(submitted.getStatus(), "ACTIVE"));
        return studentRepository.save(existing);
    }

    @Override
    @Transactional
    public Student updateProfile(String email, ProfileUpdateForm form) {
        Student student = getStudentByEmail(email);
        validateUniqueIdentity(student.getEmail(), form.getMobile(), student);
        student.setFirstName(form.getFirstName().trim());
        student.setLastName(form.getLastName().trim());
        student.setMobile(form.getMobile().trim());
        student.setCity(form.getCity().trim());
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public void changePassword(String email, PasswordChangeForm form) {
        Student student = getStudentByEmail(email);
        if (student.getPassword() == null || !passwordEncoder.matches(form.getCurrentPassword(), student.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        if (!form.passwordsMatch()) {
            throw new IllegalArgumentException("New password and confirmation do not match");
        }
        student.setPassword(passwordEncoder.encode(form.getNewPassword()));
        studentRepository.save(student);
    }

    @Override
    @Transactional
    public void deactivateStudent(Long id) {
        Student student = getStudentById(id);
        student.setStatus("INACTIVE");
        studentRepository.save(student);
    }

    private void validateUniqueIdentity(String email, String mobile, Student current) {
        String normalizedEmail = normalizeEmail(email);
        studentRepository.findByEmailIgnoreCase(normalizedEmail).ifPresent(found -> {
            if (current == null || !found.getId().equals(current.getId())) {
                throw new DuplicateResourceException("Email address is already registered");
            }
        });
        if (adminRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new DuplicateResourceException("Email address belongs to an administrator account");
        }
        if (current == null || !current.getMobile().equals(mobile)) {
            if (studentRepository.existsByMobile(mobile)) {
                throw new DuplicateResourceException("Mobile number is already registered");
            }
        }
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private String blankToDefault(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value.trim();
    }
}
