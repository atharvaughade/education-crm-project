package com.atharva.educationcrm.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atharva.educationcrm.entity.Employee;
import com.atharva.educationcrm.exception.DuplicateResourceException;
import com.atharva.educationcrm.exception.ResourceNotFoundException;
import com.atharva.educationcrm.repository.EmployeeRepository;
import com.atharva.educationcrm.service.EmployeeService;

@Service
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return employeeRepository.findAll();
        }
        String value = keyword.trim();
        return employeeRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDepartmentContainingIgnoreCase(
                value, value, value, value);
    }

    @Override
    public Employee getById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
    }

    @Override
    @Transactional
    public Employee save(Employee employee) {
        ensureUnique(employee, null);
        if (employee.getJoiningDate() == null) {
            employee.setJoiningDate(LocalDate.now());
        }
        if (employee.getStatus() == null || employee.getStatus().isBlank()) {
            employee.setStatus("ACTIVE");
        }
        employee.setEmail(employee.getEmail().trim().toLowerCase());
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public Employee update(Employee submitted) {
        Employee employee = getById(submitted.getId());
        ensureUnique(submitted, employee);
        employee.setFirstName(submitted.getFirstName().trim());
        employee.setLastName(submitted.getLastName().trim());
        employee.setEmail(submitted.getEmail().trim().toLowerCase());
        employee.setMobile(submitted.getMobile().trim());
        employee.setDesignation(submitted.getDesignation().trim());
        employee.setDepartment(submitted.getDepartment().trim());
        employee.setSalary(submitted.getSalary());
        employee.setJoiningDate(submitted.getJoiningDate());
        employee.setStatus(submitted.getStatus());
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        Employee employee = getById(id);
        employee.setStatus("INACTIVE");
        employeeRepository.save(employee);
    }

    private void ensureUnique(Employee submitted, Employee current) {
        if (current == null || !current.getEmail().equalsIgnoreCase(submitted.getEmail())) {
            if (employeeRepository.existsByEmailIgnoreCase(submitted.getEmail())) {
                throw new DuplicateResourceException("Employee email is already in use");
            }
        }
        if (current == null || !current.getMobile().equals(submitted.getMobile())) {
            if (employeeRepository.existsByMobile(submitted.getMobile())) {
                throw new DuplicateResourceException("Employee mobile number is already in use");
            }
        }
    }
}
