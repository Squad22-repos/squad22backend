package com.example.squad22backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name="user_id")
@Table(name = "professor")
public class Professor extends User {

    @ElementCollection
    @Column(columnDefinition = "TEXT[]")
    private List<String> courses;

    @ElementCollection
    @Column(columnDefinition = "TEXT[]")
    private List<String> classes;

    @ElementCollection
    @Column(columnDefinition = "TEXT[]", nullable = false)
    private List<String> degrees;

    public Professor(User newUser) {
        this.setId(newUser.getId());
        this.setName(newUser.getName());
        this.setUsername(newUser.getUsername());
        this.setAccountType(newUser.getAccountType());
        this.setActivityStatus(newUser.getActivityStatus());
        this.setRegistration(newUser.getRegistration());
    }

    public Professor() {

    }
}
