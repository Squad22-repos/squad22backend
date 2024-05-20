package com.example.squad22backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name="user_id")
@Table(name = "commercial_user")
public class CommercialUser extends User {

    @ElementCollection
    @Column(columnDefinition = "TEXT[]")
    private List<String> stores;

    @ElementCollection
    @Column(columnDefinition = "TEXT[]", nullable = false)
    private List<String> services;

    @Column(name = "opening_time")
    private LocalTime opening;

    @Column(name = "closing_time")
    private LocalTime closing;

    public CommercialUser(User newUser) {
        this.setId(newUser.getId());
        this.setName(newUser.getName());
        this.setUsername(newUser.getUsername());
        this.setAccountType(newUser.getAccountType());
        this.setActivityStatus(newUser.getActivityStatus());
        this.setRegistration(newUser.getRegistration());
    }

    public CommercialUser() {

    }
}