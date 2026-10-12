package com.atharva.educationcrm.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atharva.educationcrm.dto.AdminProfileForm;
import com.atharva.educationcrm.dto.PasswordChangeForm;
import com.atharva.educationcrm.entity.Admin;
import com.atharva.educationcrm.exception.DuplicateResourceException;
import com.atharva.educationcrm.exception.ResourceNotFoundException;
import com.atharva.educationcrm.repository.AdminRepository;
import com.atharva.educationcrm.service.AdminService;

@Service
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Admin getByEmail(String email) {
        return adminRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Admin account not found"));
    }

    @Override
    @Transactional
    public Admin updateProfile(String email, AdminProfileForm form) {
        Admin admin = getByEmail(email);
        if (!admin.getMobile().equals(form.getMobile()) && adminRepository.existsByMobile(form.getMobile())) {
            throw new DuplicateResourceException("Mobile number is already in use");
        }
        admin.setFirstName(form.getFirstName().trim());
        admin.setLastName(form.getLastName().trim());
        admin.setMobile(form.getMobile().trim());
        return adminRepository.save(admin);
    }

    @Override
    @Transactional
    public void changePassword(String email, PasswordChangeForm form) {
        Admin admin = getByEmail(email);
        validatePasswordChange(admin.getPassword(), form);
        admin.setPassword(passwordEncoder.encode(form.getNewPassword()));
        adminRepository.save(admin);
    }

    private void validatePasswordChange(String encodedPassword, PasswordChangeForm form) {
        if (!passwordEncoder.matches(form.getCurrentPassword(), encodedPassword)) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        if (!form.passwordsMatch()) {
            throw new IllegalArgumentException("New password and confirmation do not match");
        }
    }
}
