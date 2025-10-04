package ru.nsu.rush_c.services;

import ru.nsu.rush_c.payload.article.ArticleCreateRequest;
import ru.nsu.rush_c.payload.article.ArticleResponse;
import ru.nsu.rush_c.payload.clue.ClueCreateRequest;
import ru.nsu.rush_c.payload.clue.ClueResponse;

import java.util.List;

public interface ClueService {

    List<ClueResponse> getAllClue();

    ClueResponse getClueById(int id);

    ClueResponse createClue(ClueCreateRequest clueCreateRequest);

    ClueResponse deleteClue(int id);

    ClueResponse updateClue(int id, ClueCreateRequest clueCreateRequest);

}
