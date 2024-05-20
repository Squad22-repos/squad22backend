package com.example.squad22backend.models;

import com.example.squad22backend.dtos.PostCreationDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 80, updatable = false, nullable = false)
    private String id;

    @Column(name = "title", nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @Column(name = "post_date", nullable = false)
    private LocalDateTime postDate;

    @Column(name = "likes")
    private Integer likes;

    @Column(name = "visibility", length = 16, nullable = false)
    private String visibility;

    @ElementCollection
    @Column(columnDefinition = "VARCHAR(24)[]")
    private List<String> keywords;

}
