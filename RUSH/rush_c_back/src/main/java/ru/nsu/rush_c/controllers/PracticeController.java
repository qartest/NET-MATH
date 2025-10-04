package ru.nsu.rush_c.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.rush_c.payload.practice.PracticeCreateRequest;
import ru.nsu.rush_c.payload.practice.PracticeResponse;
import ru.nsu.rush_c.services.PracticeService;

import java.util.List;

@RestController
@RequestMapping("/practice")
@RequiredArgsConstructor


public class PracticeController {
    private final PracticeService practiceService;

    @GetMapping("/all")
    public ResponseEntity<List<PracticeResponse>> getAllPractices(){
        return new ResponseEntity<>(practiceService.getAllPractice(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PracticeResponse> getPracticeById(@PathVariable("id") int id){
        return new ResponseEntity<>(practiceService.getPracticeById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<PracticeResponse> createPractice(@RequestBody PracticeCreateRequest practiceCreateRequest){
        return new ResponseEntity<>(practiceService.createPractice(practiceCreateRequest), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<PracticeResponse> deletePractice(@PathVariable("id") int id){
        return new ResponseEntity<>(practiceService.deletePractice(id), HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<PracticeResponse> updatePractice(@PathVariable("id") int id, @RequestBody PracticeCreateRequest practiceCreateRequest){
        return new ResponseEntity<>(practiceService.updatePractice(id, practiceCreateRequest), HttpStatus.OK);
    }
}
