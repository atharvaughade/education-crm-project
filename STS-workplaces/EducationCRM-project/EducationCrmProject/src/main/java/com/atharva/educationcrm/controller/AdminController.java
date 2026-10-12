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

import com.atharva.educationcrm.dto.AdminProfileForm;
import com.atharva.educationcrm.dto.PasswordChangeForm;
import com.atharva.educationcrm.entity.Admin;
import com.atharva.educationcrm.service.AdminService;
import com.atharva.educationcrm.service.DashboardService;
import com.atharva.educationcrm.service.EnrollmentService;
import com.atharva.educationcrm.service.InquiryService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final DashboardService dashboardService;
    private final EnrollmentService enrollmentService;
    private final InquiryService inquiryService;

    public AdminController(AdminService adminService, DashboardService dashboardService,
            EnrollmentService enrollmentService, InquiryService inquiryService) {
        this.adminService = adminService;
        this.dashboardService = dashboardService;
        this.enrollmentService = enrollmentService;
        this.inquiryService = inquiryService;
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/auth/login?admin=true";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("stats", dashboardService.getAdminStats());
        model.addAttribute("recentPayments", enrollmentService.getAllPayments().stream().limit(5).toList());
        model.addAttribute("recentInquiries", inquiryService.getAll().stream().limit(5).toList());
        return "admin/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        populateProfile(authentication.getName(), model);
        return "admin/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute AdminProfileForm adminProfileForm,
            BindingResult bindingResult, Authentication authentication, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            populateProfile(authentication.getName(), model);
            model.addAttribute("adminProfileForm", adminProfileForm);
            return "admin/profile";
        }
        try {
            adminService.updateProfile(authentication.getName(), adminProfileForm);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully.");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/admin/profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@Valid @ModelAttribute PasswordChangeForm passwordChangeForm,
            BindingResult bindingResult, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors() || !passwordChangeForm.passwordsMatch()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Check the password fields and try again.");
            return "redirect:/admin/profile";
        }
        try {
            adminService.changePassword(authentication.getName(), passwordChangeForm);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully.");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/admin/profile";
    }

    private void populateProfile(String email, Model model) {
        Admin admin = adminService.getByEmail(email);
        model.addAttribute("admin", admin);
        if (!model.containsAttribute("adminProfileForm")) {
            AdminProfileForm form = new AdminProfileForm();
            form.setFirstName(admin.getFirstName());
            form.setLastName(admin.getLastName());
            form.setMobile(admin.getMobile());
            model.addAttribute("adminProfileForm", form);
        }
        if (!model.containsAttribute("passwordChangeForm")) {
            model.addAttribute("passwordChangeForm", new PasswordChangeForm());
        }
    }
}
