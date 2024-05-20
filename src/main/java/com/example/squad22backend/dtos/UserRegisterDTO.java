package com.example.squad22backend.dtos;

import com.example.squad22backend.models.UserRole;

import java.util.List;
import java.time.LocalTime;

public record UserRegisterDTO(
        String username,
        String name,
        String password,
        int registration,
        UserRole accountType,
        String activityStatus,
        String course,
        List<String> classes,
        List<String> courses,
        List<String> degrees,
        List<String> stores,
        List<String> services,
        LocalTime opening,
        LocalTime closing
) {}
