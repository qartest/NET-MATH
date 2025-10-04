package ru.nsu.rush_c.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.nsu.rush_c.dao.repository.ArticleRepository;
import ru.nsu.rush_c.dao.repository.ClueRepository;
import ru.nsu.rush_c.dao.repository.ModuleRepository;
import ru.nsu.rush_c.dao.repository.PracticeRepository;
import ru.nsu.rush_c.error.exception.AlreadyExsistException;
import ru.nsu.rush_c.error.exception.NotFoundException;
import ru.nsu.rush_c.models.Clue;
import ru.nsu.rush_c.models.Practice;
import ru.nsu.rush_c.models.mapper.ModelMapper;
import ru.nsu.rush_c.payload.clue.ClueCreateRequest;
import ru.nsu.rush_c.payload.clue.ClueResponse;
import ru.nsu.rush_c.services.ClueService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClueServiceImpl implements ClueService {

    private final ModelMapper modelMapper;
    private final ClueRepository clueRepository;
    private final PracticeRepository practiceRepository;

    @Transactional
    public List<ClueResponse> getAllClue() {
        List<Clue> clues = clueRepository.findAll();
        return clues.stream().map(clue -> modelMapper.toClueResponse(clue)).collect(Collectors.toList());
    }

    @Transactional
    public ClueResponse getClueById(int id) {
        Clue clue = clueRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found when found by id", id));
        return modelMapper.toClueResponse(clue);
    }

    @Transactional
    public ClueResponse createClue(ClueCreateRequest clueCreateRequest) {
        Practice practice = practiceRepository.findById(clueCreateRequest.getPractice_id()).orElseThrow(() -> new NotFoundException("Not found when found by id", clueCreateRequest.getPractice_id()));
        Clue clue = modelMapper.toClue(clueCreateRequest);
        clue.setPractice(practice);
        try{
            clue = clueRepository.save(clue);
            return modelMapper.toClueResponse(clue);
        }catch (DataIntegrityViolationException e){
            throw new AlreadyExsistException(e.getMessage());
        }
    }

    @Transactional
    public ClueResponse deleteClue(int id) {
        ClueResponse clueResponse;
        if(clueRepository.existsById(id)){
            clueResponse = modelMapper.toClueResponse(clueRepository.findById(id).orElse(null));
            clueRepository.deleteById(id);
        }else{
            throw new NotFoundException("Not found", id);
        }
        return clueResponse;
    }

    @Transactional
    public ClueResponse updateClue(int id, ClueCreateRequest clueCreateRequest) {
        Clue clue = clueRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found when found by id", id));

        clue.setDescription(clueCreateRequest.getDescription());
        try{
            clue = clueRepository.save(clue);
            return modelMapper.toClueResponse(clue);
        }catch (DataIntegrityViolationException e){
            throw new AlreadyExsistException(e.getMessage());
        }

    }
}
