package ru.nsu.rush_c.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.nsu.rush_c.dao.repository.ArticleRepository;
import ru.nsu.rush_c.dao.repository.PracticeRepository;
import ru.nsu.rush_c.error.exception.AlreadyExsistException;
import ru.nsu.rush_c.error.exception.NotFoundException;
import ru.nsu.rush_c.models.Article;
import ru.nsu.rush_c.models.Practice;
import ru.nsu.rush_c.models.User;
import ru.nsu.rush_c.models.mapper.ModelMapper;
import ru.nsu.rush_c.payload.practice.PracticeCreateRequest;
import ru.nsu.rush_c.payload.practice.PracticeResponse;
import ru.nsu.rush_c.services.PracticeService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PracticeServiceImpl implements PracticeService {

    private final ModelMapper modelMapper;
    private final PracticeRepository practiceRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public List<PracticeResponse> getAllPractice() {
        List<Practice> practices = practiceRepository.findAll();
        return practices.stream().map(practice -> modelMapper.toPracticeResponse(practice)).collect(Collectors.toList());
    }

    @Transactional
    public PracticeResponse getPracticeById(int id) {
        Practice practice = practiceRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found when found by id", id));
        return modelMapper.toPracticeResponse(practice);
    }

    @Transactional
    public PracticeResponse createPractice(PracticeCreateRequest practiceCreateRequest) {
        Article article = articleRepository.findById(practiceCreateRequest.getArticle_id()).orElseThrow(() -> new NotFoundException("Not found when found by id", practiceCreateRequest.getArticle_id()));
        Practice practice = modelMapper.toPractice(practiceCreateRequest);

        practice.setArticle(article);
        try{
            practice = practiceRepository.save(practice);
            return modelMapper.toPracticeResponse(practice);
        } catch (DataIntegrityViolationException e){
            throw new AlreadyExsistException(e.getMessage());
        }
    }

    @Transactional
    public PracticeResponse deletePractice(int id) {
        PracticeResponse practiceResponse;
        if(practiceRepository.existsById(id)){
            practiceResponse = modelMapper.toPracticeResponse(practiceRepository.findById(id).orElse(null));
            practiceRepository.deleteById(id);
        }else{
            throw new NotFoundException("Not found", id);
        }
        return practiceResponse;
    }

    @Transactional
    public PracticeResponse updatePractice(int id, PracticeCreateRequest practiceCreateRequest) {
        Practice practice = practiceRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found when found by id", id));

        practice.setName(practiceCreateRequest.getName());
        practice.setContent(practiceCreateRequest.getContent());
        practice.setCode_template(practiceCreateRequest.getCode_template());

        try{
            practice = practiceRepository.save(practice);
            return modelMapper.toPracticeResponse(practice);
        } catch (DataIntegrityViolationException e){
            throw new AlreadyExsistException(e.getMessage());
        }

    }
}
