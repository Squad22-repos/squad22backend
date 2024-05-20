package com.example.squad22backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "community_user_relation")
public class CommunityUserRelation {

    @Id
    @Column(name = "relation_id", length = 60, updatable = false, nullable = false)
    private String relationId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "community_id", referencedColumnName = "id")
    private Community communityId;

    @Column(name = "membership_status", nullable = false)
    private String membershipStatus;
}
