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

import com.atharva.educationcrm.entity.Student;
import com.atharva.educationcrm.service.StudentService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/students")
public class AdminStudentController {

    private final StudentService studentService;

    public AdminStudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        if (!model.containsAttribute("student")) model.addAttribute("student", new Student());
        return "admin/students/add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute Student student, BindingResult bindingResult,
            @RequestParam String rawPassword, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) return "admin/students/add";
        try {
            studentService.addStudent(student, rawPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Student created successfully.");
            return "redirect:/admin/students";
        } catch (RuntimeException exception) {
            bindingResult.reject("student.create", exception.getMessage());
            return "admin/students/add";
        }
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("students", studentService.searchStudents(keyword));
        model.addAttribute("keyword", keyword);
        return "admin/students/view";
    }

    @GetMapping("/edit")
    public String editForm(@RequestParam Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        return "admin/students/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute Student student, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) return "admin/students/edit";
        try {
            studentService.updateStudent(student);
            redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully.");
            return "redirect:/admin/students";
        } catch (RuntimeException exception) {
            bindingResult.reject("student.update", exception.getMessage());
            return "admin/students/edit";
        }
    }

    @PostMapping("/delete")
    public String deactivate(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        studentService.deactivateStudent(id);
        redirectAttributes.addFlashAttribute("successMessage", "Student account deactivated.");
        return "redirect:/admin/students";
    }

    @GetMapping("/details")
    public String details(@RequestParam Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        return "admin/students/view-details";
    }
}
