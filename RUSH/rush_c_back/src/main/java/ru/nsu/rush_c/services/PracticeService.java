package ru.nsu.rush_c.services;

import ru.nsu.rush_c.payload.module.ModuleCreateRequest;
import ru.nsu.rush_c.payload.module.ModuleResponse;
import ru.nsu.rush_c.payload.practice.PracticeCreateRequest;
import ru.nsu.rush_c.payload.practice.PracticeResponse;

import java.util.List;
import java.util.List;

public interface PracticeService {

    List<PracticeResponse> getAllPractice();

    PracticeResponse getPracticeById(int id);

    PracticeResponse createPractice(PracticeCreateRequest practiceCreateRequest);

    PracticeResponse deletePractice(int id);

    PracticeResponse updatePractice(int id, PracticeCreateRequest practiceCreateRequest);

}
