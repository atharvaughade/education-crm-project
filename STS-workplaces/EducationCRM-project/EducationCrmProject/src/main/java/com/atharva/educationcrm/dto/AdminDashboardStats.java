package com.atharva.educationcrm.dto;

import java.math.BigDecimal;

public record AdminDashboardStats(
        long students,
        long activeCourses,
        long employees,
        long enrollments,
        long openInquiries,
        long openFeedback,
        BigDecimal revenue) {
}
