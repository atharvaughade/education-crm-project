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

import com.atharva.educationcrm.dto.RegistrationForm;
import com.atharva.educationcrm.exception.DuplicateResourceException;
import com.atharva.educationcrm.security.CrmUserPrincipal;
import com.atharva.educationcrm.service.StudentService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth")
public class StudentAuthController {

    private final StudentService studentService;

    public StudentAuthController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        if (!model.containsAttribute("registrationForm")) {
            model.addAttribute("registrationForm", new RegistrationForm());
        }
        return "student/auth/register";
    }

    @PostMapping("/signup")
    public String register(@Valid @ModelAttribute RegistrationForm registrationForm, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (!registrationForm.passwordsMatch()) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Password and confirmation do not match");
        }
        if (bindingResult.hasErrors()) {
            return "student/auth/register";
        }
        try {
            studentService.register(registrationForm);
        } catch (DuplicateResourceException | IllegalArgumentException exception) {
            bindingResult.reject("registration.failed", exception.getMessage());
            return "student/auth/register";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Registration completed. You can now sign in.");
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            @RequestParam(required = false) String admin,
            Authentication authentication, Model model) {
        boolean adminMode = "true".equalsIgnoreCase(admin);
        if (!adminMode && authentication != null && authentication.getPrincipal() instanceof CrmUserPrincipal principal) {
            return "ADMIN".equals(principal.getRole()) ? "redirect:/admin/dashboard" : "redirect:/student/dashboard";
        }
        model.addAttribute("adminMode", adminMode);
        if (adminMode && authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("infoMessage", "You are signed in as a student. Enter administrator credentials to switch accounts.");
        }
        if (error != null) model.addAttribute("errorMessage", "Invalid credentials or inactive account.");
        if (logout != null) model.addAttribute("successMessage", "You have been signed out safely.");
        return "student/auth/login";
    }
}
