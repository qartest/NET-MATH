package ru.nsu.rush_c.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.rush_c.payload.article.ArticleCreateRequest;
import ru.nsu.rush_c.payload.article.ArticleResponse;
import ru.nsu.rush_c.payload.clue.ClueCreateRequest;
import ru.nsu.rush_c.payload.clue.ClueResponse;
import ru.nsu.rush_c.services.ClueService;

import java.util.List;

@RestController
@RequestMapping("/clue")
@RequiredArgsConstructor

public class ClueController {
    private final ClueService clueService;

    @GetMapping("/all")
    public ResponseEntity<List<ClueResponse>> getAllClues(){
        return new ResponseEntity<>(clueService.getAllClue(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ClueResponse> getClueById(@PathVariable("id") int id){
        return new ResponseEntity<>(clueService.getClueById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ClueResponse> createClue(@RequestBody ClueCreateRequest clueCreateRequest){
        return new ResponseEntity<>(clueService.createClue(clueCreateRequest), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ClueResponse> deleteClue(@PathVariable("id") int id){
        return new ResponseEntity<>(clueService.deleteClue(id), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClueResponse> updateClue(@PathVariable("id") int id, @RequestBody ClueCreateRequest clueCreateRequest){
        return new ResponseEntity<>(clueService.updateClue(id, clueCreateRequest), HttpStatus.OK);
    }


}
