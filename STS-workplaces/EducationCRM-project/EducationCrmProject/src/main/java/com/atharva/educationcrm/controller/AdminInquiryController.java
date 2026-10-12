package com.atharva.educationcrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.atharva.educationcrm.entity.Inquiry.InquiryStatus;
import com.atharva.educationcrm.service.InquiryService;

@Controller
@RequestMapping("/admin/inquiries")
public class AdminInquiryController {

    private final InquiryService inquiryService;

    public AdminInquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @GetMapping
    public String inquiries(Model model) {
        model.addAttribute("inquiries", inquiryService.getAll());
        model.addAttribute("statuses", InquiryStatus.values());
        return "admin/inquiries/view";
    }

    @PostMapping("/status")
    public String updateStatus(@RequestParam Long id, @RequestParam InquiryStatus status,
            RedirectAttributes redirectAttributes) {
        inquiryService.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("successMessage", "Inquiry status updated.");
        return "redirect:/admin/inquiries";
    }
}
