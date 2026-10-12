package com.atharva.educationcrm.service;

import java.util.List;

import com.atharva.educationcrm.entity.Employee;

public interface EmployeeService {
    List<Employee> search(String keyword);
    Employee getById(Long id);
    Employee save(Employee employee);
    Employee update(Employee employee);
    void deactivate(Long id);
}
