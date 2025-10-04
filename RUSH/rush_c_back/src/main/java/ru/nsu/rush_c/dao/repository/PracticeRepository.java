package ru.nsu.rush_c.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.rush_c.models.Practice;

public interface PracticeRepository extends JpaRepository<Practice, Integer> {
}
