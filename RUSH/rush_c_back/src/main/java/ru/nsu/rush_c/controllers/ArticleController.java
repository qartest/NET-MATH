package ru.nsu.rush_c.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.rush_c.payload.article.ArticleCreateRequest;
import ru.nsu.rush_c.payload.article.ArticleResponse;
import ru.nsu.rush_c.services.ArticleService;

import java.util.List;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor

public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/all")
    public ResponseEntity<List<ArticleResponse>> getAllArticles(){
        return new ResponseEntity<>(articleService.getAllArticle(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getArticleById(@PathVariable("id") int id){
        return new ResponseEntity<>(articleService.getArticleById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ArticleResponse> createArticle(@RequestBody ArticleCreateRequest articleCreateRequest){
        return new ResponseEntity<>(articleService.createArticle(articleCreateRequest), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ArticleResponse> deleteArticle(@PathVariable("id") int id){
        return new ResponseEntity<>(articleService.deleteArticle(id), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ArticleResponse> updateArticle(@PathVariable("id") int id, @RequestBody ArticleCreateRequest articleCreateRequest){
        return new ResponseEntity<>(articleService.updateArticle(id, articleCreateRequest), HttpStatus.OK);
    }

}
