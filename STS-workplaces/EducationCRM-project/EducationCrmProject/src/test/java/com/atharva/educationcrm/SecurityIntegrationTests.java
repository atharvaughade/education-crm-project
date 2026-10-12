package com.atharva.educationcrm;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.atharva.educationcrm.entity.Student;
import com.atharva.educationcrm.entity.Admin;
import com.atharva.educationcrm.entity.AppSetting;
import com.atharva.educationcrm.entity.Course;
import com.atharva.educationcrm.entity.Course.CourseLevel;
import com.atharva.educationcrm.entity.Course.CourseStatus;
import com.atharva.educationcrm.repository.AdminRepository;
import com.atharva.educationcrm.repository.AppSettingRepository;
import com.atharva.educationcrm.repository.CourseRepository;
import com.atharva.educationcrm.repository.StudentRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SecurityIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AppSettingRepository appSettingRepository;

    private Long courseId;

    @BeforeEach
    void createStudent() {
        if (!studentRepository.existsByEmailIgnoreCase("student@test.com")) {
            Student student = new Student();
            student.setFirstName("Test");
            student.setLastName("Student");
            student.setEmail("student@test.com");
            student.setMobile("9876543210");
            student.setCity("Pune");
            student.setCourse("Not Assigned");
            student.setAdmissionDate(LocalDate.now());
            student.setRegistrationType("SELF");
            student.setStatus("ACTIVE");
            student.setPassword(passwordEncoder.encode("Password@123"));
            studentRepository.save(student);
        }
        if (!adminRepository.existsByEmailIgnoreCase("admin@test.com")) {
            Admin admin = new Admin();
            admin.setFirstName("Test");
            admin.setLastName("Administrator");
            admin.setEmail("admin@test.com");
            admin.setMobile("9867543210");
            admin.setPassword(passwordEncoder.encode("AdminPassword@123"));
            adminRepository.save(admin);
        }
        Course course = new Course();
        course.setTitle("MVC Smoke Test Course");
        course.setCategory("Testing");
        course.setDescription("Course used to verify student and administrator Thymeleaf routes.");
        course.setPrice(new BigDecimal("999.00"));
        course.setDuration(10);
        course.setLevel(CourseLevel.BEGINNER);
        course.setStatus(CourseStatus.ACTIVE);
        courseId = courseRepository.save(course).getId();
    }

    @Test
    void publicHomeAndLoginAreAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("Active courses")));
        mockMvc.perform(get("/auth/login")).andExpect(status().isOk());
    }

    @Test
    void anonymousUserIsRedirectedFromAdminArea() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void studentCannotOpenAdminArea() throws Exception {
        mockMvc.perform(get("/admin/dashboard").with(user("student@test.com").roles("STUDENT")))
                .andExpect(status().isForbidden());
    }

    @Test
    void administratorCanOpenDashboard() throws Exception {
        mockMvc.perform(get("/admin/dashboard").with(user("admin@test.com").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Recent payments")));
    }

    @Test
    void studentCanOpenStudentHome() throws Exception {
        mockMvc.perform(get("/student/home").with(user("student@test.com").roles("STUDENT")))
                .andExpect(status().isOk());
    }

    @Test
    void stateChangingRequestRequiresCsrfToken() throws Exception {
        mockMvc.perform(post("/admin/students/delete").with(user("admin@test.com").roles("ADMIN")).param("id", "1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void studentLoginUsesDatabaseCredentialsAndRoleAwareRedirect() throws Exception {
        mockMvc.perform(post("/auth/login")
                .with(csrf())
                .param("email", "student@test.com")
                .param("password", "Password@123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/dashboard"));
    }

    @Test
    void authenticatedStudentCanOpenAdminLoginAndSwitchWithAdminCredentials() throws Exception {
        mockMvc.perform(get("/admin/login").with(user("student@test.com").roles("STUDENT")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login?admin=true"));

        mockMvc.perform(get("/auth/login").param("admin", "true")
                .with(user("student@test.com").roles("STUDENT")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Administrator sign in")));

        mockMvc.perform(post("/auth/login")
                .with(user("student@test.com").roles("STUDENT"))
                .with(csrf())
                .param("admin", "true")
                .param("email", "admin@test.com")
                .param("password", "AdminPassword@123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));
    }

    @Test
    void failedAdminLoginKeepsAdministratorMode() throws Exception {
        mockMvc.perform(post("/auth/login")
                .with(user("student@test.com").roles("STUDENT"))
                .with(csrf())
                .param("admin", "true")
                .param("email", "admin@test.com")
                .param("password", "wrong-password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login?admin=true&error=true"));
    }

    @Test
    void allAdministratorGetPagesRender() throws Exception {
        String[] routes = {
                "/admin/dashboard", "/admin/profile", "/admin/students", "/admin/students/add",
                "/admin/courses/view", "/admin/courses/add", "/admin/employees", "/admin/employees/add",
                "/admin/enrollments", "/admin/inquiries", "/admin/sales", "/admin/feedback", "/admin/settings"
        };
        for (String route : routes) {
            mockMvc.perform(get(route).with(user("admin@test.com").roles("ADMIN")))
                    .andExpect(status().isOk());
        }
        mockMvc.perform(get("/admin/courses/details").param("id", courseId.toString())
                .with(user("admin@test.com").roles("ADMIN"))).andExpect(status().isOk());
        mockMvc.perform(get("/admin/courses/edit").param("id", courseId.toString())
                .with(user("admin@test.com").roles("ADMIN"))).andExpect(status().isOk());
    }

    @Test
    void allStudentGetPagesRender() throws Exception {
        String[] routes = {"/student/dashboard", "/student/home", "/student/profile", "/student/learning", "/student/feedback"};
        for (String route : routes) {
            mockMvc.perform(get(route).with(user("student@test.com").roles("STUDENT")))
                    .andExpect(status().isOk());
        }
        mockMvc.perform(get("/student/course/" + courseId).with(user("student@test.com").roles("STUDENT")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/student/checkout").param("courseId", courseId.toString())
                .with(user("student@test.com").roles("STUDENT"))).andExpect(status().isOk());
    }

    @Test
    void disabledEnrollmentSettingBlocksCheckout() throws Exception {
        AppSetting setting = new AppSetting();
        setting.setKey("enrollment.enabled");
        setting.setValue("false");
        setting.setDescription("Test setting");
        appSettingRepository.save(setting);

        mockMvc.perform(get("/student/checkout").param("courseId", courseId.toString())
                .with(user("student@test.com").roles("STUDENT")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/course/" + courseId));
    }
}
