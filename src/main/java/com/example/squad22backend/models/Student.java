package com.example.squad22backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name="user_id")
@Table(name = "student")
public class Student extends User {

    @Column(name = "course", nullable = false, length = 64)
    private String course;

    @ElementCollection
    @Column(columnDefinition = "TEXT[]")
    private List<String> classes;

    public Student(User newUser) {
        this.setId(newUser.getId());
        this.setName(newUser.getName());
        this.setUsername(newUser.getUsername());
        this.setAccountType(newUser.getAccountType());
        this.setActivityStatus(newUser.getActivityStatus());
        this.setRegistration(newUser.getRegistration());
    }

    public Student() {

    }
}
