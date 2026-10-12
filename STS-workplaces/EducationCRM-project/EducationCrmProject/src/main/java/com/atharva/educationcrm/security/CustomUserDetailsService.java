package com.atharva.educationcrm.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atharva.educationcrm.entity.Admin;
import com.atharva.educationcrm.entity.Student;
import com.atharva.educationcrm.repository.AdminRepository;
import com.atharva.educationcrm.repository.StudentRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;

    public CustomUserDetailsService(AdminRepository adminRepository, StudentRepository studentRepository) {
        this.adminRepository = adminRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String normalizedEmail = email == null ? "" : email.trim().toLowerCase();
        return adminRepository.findByEmailIgnoreCase(normalizedEmail)
                .<UserDetails>map(this::adminPrincipal)
                .or(() -> studentRepository.findByEmailIgnoreCase(normalizedEmail).map(this::studentPrincipal))
                .orElseThrow(() -> new UsernameNotFoundException("No account found for that email"));
    }

    private CrmUserPrincipal adminPrincipal(Admin admin) {
        return new CrmUserPrincipal(admin.getId(), admin.getFullName(), admin.getEmail(), admin.getPassword(),
                "ADMIN", true);
    }

    private CrmUserPrincipal studentPrincipal(Student student) {
        if (student.getPassword() == null || student.getPassword().isBlank()) {
            throw new UsernameNotFoundException("Student account has no login password");
        }
        return new CrmUserPrincipal(student.getId(), student.getFullName(), student.getEmail(), student.getPassword(),
                "STUDENT", student.isActive());
    }
}
