package ru.nsu.rush_c.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "answer_table")
@Data
@NoArgsConstructor
@AllArgsConstructor

///  Отправляешь токены, я отправляю при регистрации акс и реф токены и при авторизации, я принимаю токены
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "count_likes", nullable = false)
    private int count_likes;

    @ManyToOne
    @JoinColumn(name = "practice_id", nullable = false)
    private Practice practice;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
