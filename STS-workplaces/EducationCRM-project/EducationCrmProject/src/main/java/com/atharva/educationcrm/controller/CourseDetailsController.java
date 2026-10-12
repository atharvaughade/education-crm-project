package com.atharva.educationcrm.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atharva.educationcrm.entity.Course;
import com.atharva.educationcrm.service.CourseService;
import com.atharva.educationcrm.service.EnrollmentService;
import com.atharva.educationcrm.service.StudentService;

@Controller
@RequestMapping("/student/course")
public class CourseDetailsController {

    private final CourseService courseService;
    private final StudentService studentService;
    private final EnrollmentService enrollmentService;

    public CourseDetailsController(CourseService courseService, StudentService studentService,
            EnrollmentService enrollmentService) {
        this.courseService = courseService;
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Authentication authentication, Model model) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("student", studentService.getStudentByEmail(authentication.getName()));
        model.addAttribute("enrolled", enrollmentService.isEnrolled(authentication.getName(), id));
        return "student/course/course-details";
    }
}
