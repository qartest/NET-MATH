package ru.nsu.rush_c.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "article_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "paid", nullable = false)
    private boolean paid;

    @Column(name = "cost", nullable = false)
    private int cost;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "data_create", nullable = false)
    private ZonedDateTime data_create;

    @Column(name = "data_update", nullable = true)
    private ZonedDateTime data_update;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private ru. nsu. rush_c. models.Module module;

    @Column(name = "number_in_module", nullable = false)
    private int number_in_module;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Practice> practices;
}
