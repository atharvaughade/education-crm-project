package com.atharva.educationcrm.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atharva.educationcrm.entity.Enrollment;
import com.atharva.educationcrm.entity.Student;
import com.atharva.educationcrm.service.CourseService;
import com.atharva.educationcrm.service.EnrollmentService;
import com.atharva.educationcrm.service.StudentService;

@Controller
@RequestMapping("/student")
public class StudentDashboardController {

    private final StudentService studentService;
    private final EnrollmentService enrollmentService;
    private final CourseService courseService;

    public StudentDashboardController(StudentService studentService, EnrollmentService enrollmentService,
            CourseService courseService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        Student student = studentService.getStudentByEmail(authentication.getName());
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(authentication.getName());
        model.addAttribute("student", student);
        model.addAttribute("enrollments", enrollments.stream().limit(3).toList());
        model.addAttribute("enrollmentCount", enrollments.size());
        model.addAttribute("completedCount", enrollments.stream().filter(item -> item.getProgress() == 100).count());
        model.addAttribute("availableCourseCount", courseService.getActiveCourses().size());
        return "student/dashboard/dashboard";
    }
}
