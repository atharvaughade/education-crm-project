package com.atharva.educationcrm.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.atharva.educationcrm.dto.PasswordChangeForm;
import com.atharva.educationcrm.dto.ProfileUpdateForm;
import com.atharva.educationcrm.entity.Student;
import com.atharva.educationcrm.service.StudentService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/student/profile")
public class StudentProfileController {

    private final StudentService studentService;

    public StudentProfileController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String profile(Authentication authentication, Model model) {
        populate(authentication.getName(), model);
        return "student/profile/profile";
    }

    @PostMapping
    public String update(@Valid @ModelAttribute ProfileUpdateForm profileUpdateForm,
            BindingResult bindingResult, Authentication authentication, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            populate(authentication.getName(), model);
            model.addAttribute("profileUpdateForm", profileUpdateForm);
            return "student/profile/profile";
        }
        try {
            studentService.updateProfile(authentication.getName(), profileUpdateForm);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully.");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/student/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute PasswordChangeForm passwordChangeForm,
            BindingResult bindingResult, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors() || !passwordChangeForm.passwordsMatch()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Check the password fields and try again.");
            return "redirect:/student/profile";
        }
        try {
            studentService.changePassword(authentication.getName(), passwordChangeForm);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully.");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/student/profile";
    }

    private void populate(String email, Model model) {
        Student student = studentService.getStudentByEmail(email);
        model.addAttribute("student", student);
        if (!model.containsAttribute("profileUpdateForm")) {
            ProfileUpdateForm form = new ProfileUpdateForm();
            form.setFirstName(student.getFirstName());
            form.setLastName(student.getLastName());
            form.setMobile(student.getMobile());
            form.setCity(student.getCity());
            model.addAttribute("profileUpdateForm", form);
        }
        if (!model.containsAttribute("passwordChangeForm")) {
            model.addAttribute("passwordChangeForm", new PasswordChangeForm());
        }
    }
}
