package com.atharva.educationcrm.service;

import com.atharva.educationcrm.dto.AdminProfileForm;
import com.atharva.educationcrm.dto.PasswordChangeForm;
import com.atharva.educationcrm.entity.Admin;

public interface AdminService {
    Admin getByEmail(String email);
    Admin updateProfile(String email, AdminProfileForm form);
    void changePassword(String email, PasswordChangeForm form);
}
