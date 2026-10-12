package com.atharva.educationcrm.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.atharva.educationcrm.service.EnrollmentService;
import com.atharva.educationcrm.service.StudentService;

@Controller
@RequestMapping("/student/learning")
public class StudentLearningController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;

    public StudentLearningController(EnrollmentService enrollmentService, StudentService studentService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
    }

    @GetMapping
    public String learning(Authentication authentication, Model model) {
        model.addAttribute("student", studentService.getStudentByEmail(authentication.getName()));
        model.addAttribute("enrollments", enrollmentService.getStudentEnrollments(authentication.getName()));
        model.addAttribute("payments", enrollmentService.getStudentPayments(authentication.getName()));
        return "student/learning/my-courses";
    }

    @PostMapping("/progress")
    public String updateProgress(@RequestParam Long enrollmentId, @RequestParam int progress,
            Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            enrollmentService.updateProgress(authentication.getName(), enrollmentId, progress);
            redirectAttributes.addFlashAttribute("successMessage", "Learning progress updated.");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/student/learning";
    }
}
