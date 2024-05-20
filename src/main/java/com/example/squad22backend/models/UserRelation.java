package com.example.squad22backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_relation")
public class UserRelation {

    @Id
    @Column(name = "relation_id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "actor_id", referencedColumnName = "id")
    private User actor;

    @ManyToOne
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    private User subject;

    @Column(name = "relation_type")
    private String relationship;
}