package com.atharva.educationcrm.initializer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.atharva.educationcrm.entity.Admin;
import com.atharva.educationcrm.repository.AdminRepository;

@Component
public class AdminDataInitializer implements ApplicationRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap.admin.enabled:false}")
    private boolean enabled;

    @Value("${app.bootstrap.admin.email:}")
    private String email;

    @Value("${app.bootstrap.admin.password:}")
    private String password;

    @Value("${app.bootstrap.admin.mobile:}")
    private String mobile;

    public AdminDataInitializer(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!enabled || email.isBlank() || password.isBlank() || mobile.isBlank()
                || adminRepository.existsByEmailIgnoreCase(email)) {
            return;
        }
        Admin admin = new Admin();
        admin.setFirstName("System");
        admin.setLastName("Administrator");
        admin.setEmail(email.trim().toLowerCase());
        admin.setMobile(mobile.trim());
        admin.setPassword(passwordEncoder.encode(password));
        adminRepository.save(admin);
    }
}
