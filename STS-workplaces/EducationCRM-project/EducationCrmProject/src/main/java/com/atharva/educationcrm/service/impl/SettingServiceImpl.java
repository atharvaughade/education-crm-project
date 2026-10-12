package com.atharva.educationcrm.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atharva.educationcrm.entity.AppSetting;
import com.atharva.educationcrm.repository.AppSettingRepository;
import com.atharva.educationcrm.service.SettingService;

@Service
@Transactional(readOnly = true)
public class SettingServiceImpl implements SettingService {

    private static final Map<String, String> DEFAULTS = Map.of(
            "academy.name", "Education CRM Academy",
            "academy.email", "support@example.com",
            "academy.phone", "9999999999",
            "academy.address", "Pune, Maharashtra",
            "enrollment.enabled", "true");

    private final AppSettingRepository settingRepository;

    public SettingServiceImpl(AppSettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @Override
    public List<AppSetting> getAll() {
        return settingRepository.findAll();
    }

    @Override
    public Map<String, String> getAsMap() {
        Map<String, String> values = new LinkedHashMap<>(DEFAULTS);
        settingRepository.findAll().forEach(setting -> values.put(setting.getKey(), setting.getValue()));
        return values;
    }

    @Override
    public String getValue(String key) {
        return settingRepository.findByKey(key)
                .map(AppSetting::getValue)
                .orElse(DEFAULTS.get(key));
    }

    @Override
    public boolean isEnabled(String key) {
        return Boolean.parseBoolean(getValue(key));
    }

    @Override
    @Transactional
    public void update(Map<String, String> values) {
        DEFAULTS.keySet().forEach(key -> {
            String value = values.getOrDefault(key, DEFAULTS.get(key)).trim();
            AppSetting setting = settingRepository.findByKey(key).orElseGet(AppSetting::new);
            setting.setKey(key);
            setting.setValue(value);
            setting.setDescription(descriptionFor(key));
            settingRepository.save(setting);
        });
    }

    private String descriptionFor(String key) {
        return switch (key) {
            case "academy.name" -> "Academy display name";
            case "academy.email" -> "Public support email";
            case "academy.phone" -> "Public support phone";
            case "academy.address" -> "Public address";
            case "enrollment.enabled" -> "Allow students to complete demo checkout";
            default -> "Application setting";
        };
    }
}
