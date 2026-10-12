package com.atharva.educationcrm.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.atharva.educationcrm.service.SettingService;

@Controller
@RequestMapping("/admin/settings")
public class AdminSettingsController {

    private final SettingService settingService;

    public AdminSettingsController(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping
    public String settings(Model model) {
        model.addAttribute("settings", settingService.getAsMap());
        return "admin/settings/view";
    }

    @PostMapping
    public String update(@RequestParam Map<String, String> values, RedirectAttributes redirectAttributes) {
        settingService.update(values);
        redirectAttributes.addFlashAttribute("successMessage", "Settings saved successfully.");
        return "redirect:/admin/settings";
    }
}
