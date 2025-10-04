package ru.nsu.rush_c.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clue_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "practice_id", nullable = false)
    private Practice practice;
}
