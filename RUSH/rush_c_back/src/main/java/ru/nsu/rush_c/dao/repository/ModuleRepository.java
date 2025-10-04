package ru.nsu.rush_c.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.rush_c.models.Module;

public interface ModuleRepository extends JpaRepository<Module, Integer> {
}
