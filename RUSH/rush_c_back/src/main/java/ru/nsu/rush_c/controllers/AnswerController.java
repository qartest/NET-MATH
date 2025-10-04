package ru.nsu.rush_c.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.rush_c.payload.answer.AnswerCreateRequest;
import ru.nsu.rush_c.payload.answer.AnswerResponse;
import ru.nsu.rush_c.payload.article.ArticleCreateRequest;
import ru.nsu.rush_c.payload.article.ArticleResponse;
import ru.nsu.rush_c.payload.user.UserCreateRequest;
import ru.nsu.rush_c.services.AnswerService;

import java.util.List;

@RestController
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    @GetMapping("/all")
    public ResponseEntity<List<AnswerResponse>> getAllArticles(){
        return new ResponseEntity<>(answerService.getAllAnswer(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerResponse> getArticleById(@PathVariable("id") int id){
        return new ResponseEntity<>(answerService.getAnswerById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<AnswerResponse> createArticle(@RequestBody AnswerCreateRequest answerCreateRequest){
        return new ResponseEntity<>(answerService.createAnswer(answerCreateRequest), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<AnswerResponse> deleteAnswer(@PathVariable("id") int id){
        return new ResponseEntity<>(answerService.deleteAnswer(id), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AnswerResponse> updateAnswer(@PathVariable("id") int id, @RequestBody AnswerCreateRequest answerCreateRequest){
        return new ResponseEntity<>(answerService.updateAnswer(id, answerCreateRequest), HttpStatus.OK);
    }
}
