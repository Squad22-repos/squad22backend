package com.example.squad22backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "community")
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(name = "uuid")
    @Column(name = "id", length = 64, updatable = false, nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "creator", referencedColumnName = "id")
    private User creator;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "theme", nullable = false)
    private String theme;

    @Column(name = "description")
    private String description;

    @Column(name = "visibility", nullable = false)
    private String visibility;
}
