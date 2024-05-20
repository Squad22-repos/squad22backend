package com.example.squad22backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "user_post_interaction")
public class UserPostRelation {

    @Id
    @Column(name = "interaction_id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User actor;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @Column(name = "action_type", length = 16, nullable = false)
    private String actionType;

    @Column(name = "action_status", length = 16)
    private String actionStatus;

    @Column(name = "is_liked")
    private Boolean isLiked;

    @Column(name = "is_commented")
    private Boolean isCommented;
}
