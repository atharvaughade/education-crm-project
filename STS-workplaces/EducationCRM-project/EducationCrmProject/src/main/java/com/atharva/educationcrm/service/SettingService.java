package com.atharva.educationcrm.service;

import java.util.List;
import java.util.Map;

import com.atharva.educationcrm.entity.AppSetting;

public interface SettingService {
    List<AppSetting> getAll();
    Map<String, String> getAsMap();
    String getValue(String key);
    boolean isEnabled(String key);
    void update(Map<String, String> values);
}
