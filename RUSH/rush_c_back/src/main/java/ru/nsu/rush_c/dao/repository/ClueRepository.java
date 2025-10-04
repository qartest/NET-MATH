package ru.nsu.rush_c.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.rush_c.models.Clue;

public interface ClueRepository extends JpaRepository<Clue, Integer> {
}
