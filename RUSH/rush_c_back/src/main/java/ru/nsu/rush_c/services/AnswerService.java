package ru.nsu.rush_c.services;

import ru.nsu.rush_c.payload.answer.AnswerCreateRequest;
import ru.nsu.rush_c.payload.answer.AnswerResponse;
import ru.nsu.rush_c.payload.article.ArticleCreateRequest;
import ru.nsu.rush_c.payload.article.ArticleResponse;

import java.util.List;

public interface AnswerService {

    List<AnswerResponse> getAllAnswer();

    AnswerResponse getAnswerById(int id);

    AnswerResponse createAnswer(AnswerCreateRequest answerCreateRequest);

    AnswerResponse deleteAnswer(int id);

    AnswerResponse updateAnswer(int id,  AnswerCreateRequest answerCreateRequest);
}
