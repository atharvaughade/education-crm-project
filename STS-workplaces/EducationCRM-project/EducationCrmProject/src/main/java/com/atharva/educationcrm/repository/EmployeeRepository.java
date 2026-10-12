package com.atharva.educationcrm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.atharva.educationcrm.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByMobile(String mobile);
    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDepartmentContainingIgnoreCase(
            String firstName, String lastName, String email, String department);
}
