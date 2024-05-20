package com.example.squad22backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "community_post")
public class CommunityPostRelation {

    @Id
    @Column(name = "relation_id")
    private String relationId;

    @ManyToOne
    @JoinColumn(name = "community_id", referencedColumnName = "id")
    private Community community;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @Column(name = "post_status", length = 16, nullable = false)
    private String postStatus;
}
