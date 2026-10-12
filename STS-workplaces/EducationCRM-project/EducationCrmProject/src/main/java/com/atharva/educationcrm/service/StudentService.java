package com.atharva.educationcrm.service;

import java.util.List;

import com.atharva.educationcrm.dto.PasswordChangeForm;
import com.atharva.educationcrm.dto.ProfileUpdateForm;
import com.atharva.educationcrm.dto.RegistrationForm;
import com.atharva.educationcrm.entity.Student;

public interface StudentService {
    Student register(RegistrationForm form);
    Student addStudent(Student student, String rawPassword);
    List<Student> getAllStudents();
    List<Student> searchStudents(String keyword);
    Student getStudentById(Long id);
    Student getStudentByEmail(String email);
    Student updateStudent(Student student);
    Student updateProfile(String email, ProfileUpdateForm form);
    void changePassword(String email, PasswordChangeForm form);
    void deactivateStudent(Long id);
}
