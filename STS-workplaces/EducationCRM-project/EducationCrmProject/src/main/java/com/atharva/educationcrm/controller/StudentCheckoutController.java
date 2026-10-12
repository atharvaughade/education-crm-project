package com.atharva.educationcrm.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.atharva.educationcrm.dto.CheckoutForm;
import com.atharva.educationcrm.entity.Course;
import com.atharva.educationcrm.entity.Payment.PaymentMethod;
import com.atharva.educationcrm.service.CourseService;
import com.atharva.educationcrm.service.EnrollmentService;
import com.atharva.educationcrm.service.SettingService;
import com.atharva.educationcrm.service.StudentService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/student/checkout")
public class StudentCheckoutController {

    private final CourseService courseService;
    private final StudentService studentService;
    private final EnrollmentService enrollmentService;
    private final SettingService settingService;

    public StudentCheckoutController(CourseService courseService, StudentService studentService,
            EnrollmentService enrollmentService, SettingService settingService) {
        this.courseService = courseService;
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
        this.settingService = settingService;
    }

    @GetMapping
    public String checkout(@RequestParam Long courseId, Authentication authentication, Model model,
            RedirectAttributes redirectAttributes) {
        if (!settingService.isEnabled("enrollment.enabled")) {
            redirectAttributes.addFlashAttribute("errorMessage", "New enrollments are temporarily disabled.");
            return "redirect:/student/course/" + courseId;
        }
        if (enrollmentService.isEnrolled(authentication.getName(), courseId)) {
            redirectAttributes.addFlashAttribute("infoMessage", "You are already enrolled in this course.");
            return "redirect:/student/learning";
        }
        CheckoutForm form = new CheckoutForm();
        form.setCourseId(courseId);
        form.setPaymentMethod(PaymentMethod.UPI);
        model.addAttribute("checkoutForm", form);
        populate(courseId, authentication.getName(), model);
        return "student/checkout/checkout";
    }

    @PostMapping
    public String complete(@Valid @ModelAttribute CheckoutForm checkoutForm, BindingResult bindingResult,
            Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        if (!settingService.isEnabled("enrollment.enabled")) {
            redirectAttributes.addFlashAttribute("errorMessage", "New enrollments are temporarily disabled.");
            return "redirect:/student/course/" + checkoutForm.getCourseId();
        }
        if (bindingResult.hasErrors()) {
            populate(checkoutForm.getCourseId(), authentication.getName(), model);
            return "student/checkout/checkout";
        }
        try {
            var payment = enrollmentService.checkout(authentication.getName(), checkoutForm);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Enrollment completed. Payment reference: " + payment.getReference());
            return "redirect:/student/learning";
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/student/course/" + checkoutForm.getCourseId();
        }
    }

    private void populate(Long courseId, String email, Model model) {
        Course course = courseService.getCourseById(courseId);
        model.addAttribute("course", course);
        model.addAttribute("student", studentService.getStudentByEmail(email));
        model.addAttribute("paymentMethods", PaymentMethod.values());
    }
}
