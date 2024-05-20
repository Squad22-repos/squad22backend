package com.example.squad22backend.models;

import lombok.Getter;

@Getter
public enum UserRole {
    PROFESSOR("professor"),
    ESTUDANTE("estudante"),
    COMERCIAL("comercial"),
    ADMIN("admin");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

}
