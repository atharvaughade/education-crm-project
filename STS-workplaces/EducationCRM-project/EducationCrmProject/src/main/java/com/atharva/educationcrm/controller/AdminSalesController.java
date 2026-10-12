package com.atharva.educationcrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atharva.educationcrm.service.DashboardService;
import com.atharva.educationcrm.service.EnrollmentService;

@Controller
@RequestMapping("/admin/sales")
public class AdminSalesController {

    private final EnrollmentService enrollmentService;
    private final DashboardService dashboardService;

    public AdminSalesController(EnrollmentService enrollmentService, DashboardService dashboardService) {
        this.enrollmentService = enrollmentService;
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public String sales(Model model) {
        var payments = enrollmentService.getAllPayments();
        model.addAttribute("payments", payments);
        model.addAttribute("revenue", dashboardService.getAdminStats().revenue());
        model.addAttribute("paymentCount", payments.size());
        return "admin/sales/view";
    }
}
