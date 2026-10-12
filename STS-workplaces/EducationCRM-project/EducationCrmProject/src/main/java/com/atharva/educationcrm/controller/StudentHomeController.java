package com.atharva.educationcrm.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atharva.educationcrm.service.CourseService;
import com.atharva.educationcrm.service.StudentService;

@Controller
@RequestMapping("/student")
public class StudentHomeController {

    private final StudentService studentService;
    private final CourseService courseService;

    public StudentHomeController(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        model.addAttribute("student", studentService.getStudentByEmail(authentication.getName()));
        model.addAttribute("courses", courseService.getActiveCourses());
        return "student/home/home";
    }
}
