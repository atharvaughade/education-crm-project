package com.atharva.educationcrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.atharva.educationcrm.entity.Inquiry;
import com.atharva.educationcrm.service.CourseService;
import com.atharva.educationcrm.service.InquiryService;
import com.atharva.educationcrm.service.SettingService;

import jakarta.validation.Valid;

@Controller
public class PublicController {

    private final CourseService courseService;
    private final InquiryService inquiryService;
    private final SettingService settingService;

    public PublicController(CourseService courseService, InquiryService inquiryService, SettingService settingService) {
        this.courseService = courseService;
        this.inquiryService = inquiryService;
        this.settingService = settingService;
    }

    @GetMapping("/")
    public String home(Model model) {
        populateHome(model);
        if (!model.containsAttribute("inquiry")) {
            model.addAttribute("inquiry", new Inquiry());
        }
        return "public/index";
    }

    @PostMapping("/inquiries")
    public String submitInquiry(@Valid Inquiry inquiry, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            populateHome(model);
            return "public/index";
        }
        inquiryService.create(inquiry);
        redirectAttributes.addFlashAttribute("successMessage", "Thank you. Our admissions team will contact you soon.");
        return "redirect:/#contact";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }

    private void populateHome(Model model) {
        model.addAttribute("courses", courseService.getActiveCourses());
        model.addAttribute("settings", settingService.getAsMap());
    }
}
