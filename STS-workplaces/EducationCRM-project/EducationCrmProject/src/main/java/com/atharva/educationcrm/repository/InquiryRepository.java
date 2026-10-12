package com.atharva.educationcrm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.atharva.educationcrm.entity.Inquiry;
import com.atharva.educationcrm.entity.Inquiry.InquiryStatus;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findAllByOrderByCreatedAtDesc();
    long countByStatus(InquiryStatus status);
}
