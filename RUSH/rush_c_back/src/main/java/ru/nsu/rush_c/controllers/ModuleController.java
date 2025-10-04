package ru.nsu.rush_c.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.rush_c.models.Module;
import ru.nsu.rush_c.payload.module.ModuleCreateRequest;
import ru.nsu.rush_c.payload.module.ModuleResponse;
import ru.nsu.rush_c.payload.user.UserCreateRequest;
import ru.nsu.rush_c.services.ModuleService;


import java.util.List;

@RestController
@RequestMapping("/module")
@RequiredArgsConstructor

public class ModuleController {
    private final ModuleService moduleService;


    @GetMapping("/all")
    public ResponseEntity<List<ModuleResponse>> getCatalog(){
        return new ResponseEntity<>(moduleService.getAllModule(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleResponse> getModuleById(@PathVariable("id") int id){
        return new ResponseEntity<>(moduleService.getModuleById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ModuleResponse> createModule(@RequestBody ModuleCreateRequest moduleCreateRequest){
        return new ResponseEntity<>(moduleService.createModule(moduleCreateRequest), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ModuleResponse> deleteModule(@PathVariable("id") int id){
        return new ResponseEntity<>(moduleService.deleteModule(id), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ModuleResponse> updateModule(@PathVariable("id") int id, @RequestBody ModuleCreateRequest moduleCreateRequest){
        return new ResponseEntity<>(moduleService.updateModule(id, moduleCreateRequest), HttpStatus.OK);
    }
}
