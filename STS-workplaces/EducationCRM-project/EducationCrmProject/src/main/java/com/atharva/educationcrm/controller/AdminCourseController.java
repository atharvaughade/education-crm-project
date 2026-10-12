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

import com.atharva.educationcrm.entity.Course;
import com.atharva.educationcrm.entity.Course.CourseLevel;
import com.atharva.educationcrm.entity.Course.CourseStatus;
import com.atharva.educationcrm.service.CourseService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/courses")
public class AdminCourseController {

    private final CourseService courseService;

    public AdminCourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        if (!model.containsAttribute("course")) model.addAttribute("course", new Course());
        addOptions(model);
        return "admin/courses/add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute Course course, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addOptions(model);
            return "admin/courses/add";
        }
        courseService.addCourse(course);
        redirectAttributes.addFlashAttribute("successMessage", "Course created successfully.");
        return "redirect:/admin/courses/view";
    }

    @GetMapping("/view")
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("courses", courseService.searchCourses(keyword));
        model.addAttribute("keyword", keyword);
        return "admin/courses/view";
    }

    @GetMapping("/edit")
    public String editForm(@RequestParam Long id, Model model) {
        model.addAttribute("course", courseService.getCourseById(id));
        addOptions(model);
        return "admin/courses/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute Course course, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addOptions(model);
            return "admin/courses/edit";
        }
        courseService.updateCourse(course);
        redirectAttributes.addFlashAttribute("successMessage", "Course updated successfully.");
        return "redirect:/admin/courses/view";
    }

    @PostMapping("/delete")
    public String deactivate(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        courseService.deactivateCourse(id);
        redirectAttributes.addFlashAttribute("successMessage", "Course deactivated.");
        return "redirect:/admin/courses/view";
    }

    @GetMapping("/details")
    public String details(@RequestParam Long id, Model model) {
        model.addAttribute("course", courseService.getCourseById(id));
        return "admin/courses/view-details";
    }

    private void addOptions(Model model) {
        model.addAttribute("levels", CourseLevel.values());
        model.addAttribute("statuses", CourseStatus.values());
    }
}
