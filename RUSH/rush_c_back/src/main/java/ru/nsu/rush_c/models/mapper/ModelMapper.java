package ru.nsu.rush_c.models.mapper;

import java.util.Base64;
import org.mapstruct.*;
import ru.nsu.rush_c.models.*;
import ru.nsu.rush_c.payload.answer.AnswerCreateRequest;
import ru.nsu.rush_c.payload.answer.AnswerResponse;
import ru.nsu.rush_c.payload.article.ArticleCreateRequest;
import ru.nsu.rush_c.payload.article.ArticleResponse;
import ru.nsu.rush_c.payload.clue.ClueCreateRequest;
import ru.nsu.rush_c.payload.clue.ClueResponse;
import ru.nsu.rush_c.payload.module.ModuleCreateRequest;
import ru.nsu.rush_c.payload.module.ModuleResponse;
import ru.nsu.rush_c.payload.practice.PracticeCreateRequest;
import ru.nsu.rush_c.payload.practice.PracticeResponse;
import ru.nsu.rush_c.payload.user.UserCreateRequest;
import ru.nsu.rush_c.payload.user.UserResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ModelMapper {

    default ru.nsu.rush_c.models.Module map(ru.nsu.rush_c.models.Module input){
        return input;
    }

    @Mapping(source = "role", target = "role")
    UserResponse toUserResponse(User user);

    @Mapping(source = "role", target = "role")
    User toUser (UserCreateRequest userCreateRequest);

    @Mapping(source = "user.id", target = "user_id")
    @Mapping(source = "module.id", target = "module_id")
    ArticleResponse toArticleResponse(Article article);

    Article toArticle(ArticleCreateRequest articleCreateRequest);

    ModuleResponse toModuleResponse(ru.nsu.rush_c.models.Module module);

    ru.nsu.rush_c.models.Module toModule(ModuleCreateRequest moduleCreateRequest);

    Practice toPractice(PracticeCreateRequest practiceCreateRequest);

    @Mapping(source = "article.id", target = "article_id")
    PracticeResponse toPracticeResponse(Practice practice);

    @Mapping(source = "practice.id", target = "practice_id")
    ClueResponse toClueResponse(Clue clue);

    Clue toClue(ClueCreateRequest clueCreateRequest);

    Answer toAnswer(AnswerCreateRequest answerCreateRequest);

    AnswerResponse toAnswerResponse(Answer answer);
}
