package com.atharva.educationcrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atharva.educationcrm.service.EnrollmentService;

@Controller
@RequestMapping("/admin/enrollments")
public class AdminEnrollmentController {

    private final EnrollmentService enrollmentService;

    public AdminEnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public String enrollments(Model model) {
        model.addAttribute("enrollments", enrollmentService.getAllEnrollments());
        return "admin/enrollments/view";
    }
}
