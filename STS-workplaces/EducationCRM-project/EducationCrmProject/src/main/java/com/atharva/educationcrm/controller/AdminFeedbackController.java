package com.atharva.educationcrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.atharva.educationcrm.entity.Feedback.FeedbackStatus;
import com.atharva.educationcrm.service.FeedbackService;

@Controller
@RequestMapping("/admin/feedback")
public class AdminFeedbackController {

    private final FeedbackService feedbackService;

    public AdminFeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public String feedback(Model model) {
        model.addAttribute("feedbackItems", feedbackService.getAll());
        model.addAttribute("statuses", FeedbackStatus.values());
        return "admin/feedback/view";
    }

    @PostMapping("/respond")
    public String respond(@RequestParam Long id, @RequestParam FeedbackStatus status,
            @RequestParam(required = false) String response, RedirectAttributes redirectAttributes) {
        feedbackService.respond(id, status, response);
        redirectAttributes.addFlashAttribute("successMessage", "Feedback updated.");
        return "redirect:/admin/feedback";
    }
}
