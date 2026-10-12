package com.atharva.educationcrm.service;

import java.util.List;

import com.atharva.educationcrm.entity.Inquiry;
import com.atharva.educationcrm.entity.Inquiry.InquiryStatus;

public interface InquiryService {
    Inquiry create(Inquiry inquiry);
    List<Inquiry> getAll();
    Inquiry updateStatus(Long id, InquiryStatus status);
}
