package ru.nsu.rush_c.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.nsu.rush_c.dao.repository.ArticleRepository;
import ru.nsu.rush_c.dao.repository.ModuleRepository;
import ru.nsu.rush_c.dao.repository.UserRepository;
import ru.nsu.rush_c.error.exception.AlreadyExsistException;
import ru.nsu.rush_c.error.exception.NotFoundException;
import ru.nsu.rush_c.models.Article;
import ru.nsu.rush_c.models.Module;
import ru.nsu.rush_c.models.User;
import ru.nsu.rush_c.models.mapper.ModelMapper;
import ru.nsu.rush_c.payload.article.ArticleCreateRequest;
import ru.nsu.rush_c.payload.article.ArticleResponse;
import ru.nsu.rush_c.services.ArticleService;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public List<ArticleResponse> getAllArticle() {
        List<Article> articles = articleRepository.findAll();
        return articles.stream().map(article -> modelMapper.toArticleResponse(article)).collect(Collectors.toList());
    }

    @Transactional
    public ArticleResponse getArticleById(int id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found when found by id", id));
        return modelMapper.toArticleResponse(article);
    }

    @Transactional
    public ArticleResponse createArticle(ArticleCreateRequest articleCreateRequest) {
        Module module = moduleRepository.findById(articleCreateRequest.getModule_id()).orElseThrow(() -> new NotFoundException("Not found when found by id", articleCreateRequest.getModule_id()));
        User user = userRepository.findById(articleCreateRequest.getUser_id()).orElseThrow(() -> new NotFoundException("Not found when found by id", articleCreateRequest.getUser_id()));

        Article article = modelMapper.toArticle(articleCreateRequest);
        article.setData_update(null);

        article.setModule(module);
        article.setUser(user);

        article.setData_create(ZonedDateTime.now());

        try{
            article = articleRepository.save(article);
            return modelMapper.toArticleResponse(article);
        } catch (DataIntegrityViolationException e){
            throw new AlreadyExsistException(e.getMessage());
        }
    }

    @Transactional
    public ArticleResponse deleteArticle(int id) {
        ArticleResponse articleResponse;
        if(articleRepository.existsById(id)){
            articleResponse = modelMapper.toArticleResponse(articleRepository.findById(id).orElse(null));
            articleRepository.deleteById(id);
        }
        else{
            throw new NotFoundException("Not found", id);
        }
        return articleResponse;
    }

    @Transactional
    public ArticleResponse updateArticle(int id, ArticleCreateRequest articleCreateRequest) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found when found by id", id));

        article.setContent(articleCreateRequest.getContent());
        article.setName(articleCreateRequest.getName());
        article.setCost(articleCreateRequest.getCost());
        article.setNumber_in_module(article.getNumber_in_module());

        article.setData_update(ZonedDateTime.now());

        try{
            article = articleRepository.save(article);
            return modelMapper.toArticleResponse(article);
        } catch (DataIntegrityViolationException e){
            throw new AlreadyExsistException(e.getMessage());
        }
    }
}
