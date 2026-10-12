package com.atharva.educationcrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.atharva.educationcrm.entity.Employee;
import com.atharva.educationcrm.service.EmployeeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/employees")
public class AdminEmployeeController {

    private final EmployeeService employeeService;

    public AdminEmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("employees", employeeService.search(keyword));
        model.addAttribute("keyword", keyword);
        return "admin/employees/view";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "admin/employees/form";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute Employee employee, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) return "admin/employees/form";
        try {
            employeeService.save(employee);
            redirectAttributes.addFlashAttribute("successMessage", "Employee added successfully.");
            return "redirect:/admin/employees";
        } catch (RuntimeException exception) {
            bindingResult.reject("employee.create", exception.getMessage());
            return "admin/employees/form";
        }
    }

    @GetMapping("/edit")
    public String editForm(@RequestParam Long id, Model model) {
        model.addAttribute("employee", employeeService.getById(id));
        return "admin/employees/form";
    }

    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute Employee employee, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) return "admin/employees/form";
        try {
            employeeService.update(employee);
            redirectAttributes.addFlashAttribute("successMessage", "Employee updated successfully.");
            return "redirect:/admin/employees";
        } catch (RuntimeException exception) {
            bindingResult.reject("employee.update", exception.getMessage());
            return "admin/employees/form";
        }
    }

    @PostMapping("/delete")
    public String deactivate(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        employeeService.deactivate(id);
        redirectAttributes.addFlashAttribute("successMessage", "Employee deactivated.");
        return "redirect:/admin/employees";
    }
}
