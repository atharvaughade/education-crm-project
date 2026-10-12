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

import com.atharva.educationcrm.entity.Feedback;
import com.atharva.educationcrm.service.FeedbackService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/student/feedback")
public class StudentFeedbackController {

    private final FeedbackService feedbackService;

    public StudentFeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public String feedback(Authentication authentication, Model model) {
        if (!model.containsAttribute("feedbackForm")) model.addAttribute("feedbackForm", new Feedback());
        model.addAttribute("feedbackItems", feedbackService.getForStudent(authentication.getName()));
        return "student/feedback/feedback";
    }

    @PostMapping
    public String submit(@Valid @ModelAttribute("feedbackForm") Feedback feedback, BindingResult bindingResult,
            Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("feedbackItems", feedbackService.getForStudent(authentication.getName()));
            return "student/feedback/feedback";
        }
        feedbackService.create(authentication.getName(), feedback);
        redirectAttributes.addFlashAttribute("successMessage", "Thank you for your feedback.");
        return "redirect:/student/feedback";
    }
}
