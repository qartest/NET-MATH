package ru.nsu.rush_c.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.nsu.rush_c.dao.repository.AnswerRepository;
import ru.nsu.rush_c.dao.repository.PracticeRepository;
import ru.nsu.rush_c.dao.repository.UserRepository;
import ru.nsu.rush_c.error.exception.AlreadyExsistException;
import ru.nsu.rush_c.error.exception.NotFoundException;
import ru.nsu.rush_c.models.Answer;
import ru.nsu.rush_c.models.Practice;
import ru.nsu.rush_c.models.User;
import ru.nsu.rush_c.models.mapper.ModelMapper;
import ru.nsu.rush_c.payload.answer.AnswerCreateRequest;
import ru.nsu.rush_c.payload.answer.AnswerResponse;
import ru.nsu.rush_c.services.AnswerService;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final ModelMapper modelMapper;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final PracticeRepository practiceRepository;


    @Transactional
    public List<AnswerResponse> getAllAnswer() {
        List<Answer> answers = answerRepository.findAll();
        return answers.stream().map(answer -> modelMapper.toAnswerResponse(answer)).collect(Collectors.toList());
    }

    @Transactional
    public AnswerResponse getAnswerById(int id) {
        Answer answer = answerRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found when found by id", id));
        return modelMapper.toAnswerResponse(answer);
    }

    @Transactional
    public AnswerResponse createAnswer(AnswerCreateRequest answerCreateRequest) {
        User user = userRepository.findById(answerCreateRequest.getUser_id()).orElseThrow(() -> new NotFoundException("Not found when found by id", answerCreateRequest.getUser_id()));
        Practice practice = practiceRepository.findById(answerCreateRequest.getPractice_id()).orElseThrow(() -> new NotFoundException("Not found when found by id", answerCreateRequest.getPractice_id()));

        Answer answer = modelMapper.toAnswer(answerCreateRequest);
        answer.setCount_likes(0);
        answer.setUser(user);
        answer.setPractice(practice);

        try{
            answer = answerRepository.save(answer);
            return modelMapper.toAnswerResponse(answer);
        } catch (DataIntegrityViolationException e){
            throw new AlreadyExsistException(e.getMessage());
        }
    }

    @Transactional
    public AnswerResponse deleteAnswer(int id) {
        AnswerResponse answerResponse;
        if(answerRepository.existsById(id)){
            answerResponse = modelMapper.toAnswerResponse(answerRepository.findById(id).orElse(null));
            answerRepository.deleteById(id);
        } else {
            throw new NotFoundException("Not found", id);
        }
        return answerResponse;
    }

    @Transactional
    public AnswerResponse updateAnswer(int id, AnswerCreateRequest answerCreateRequest) {
        Answer answer = answerRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found when found by id", id));
        answer.setCount_likes(answerCreateRequest.getCount_likes());
        answer.setContent(answerCreateRequest.getContent());

        try{
            answer = answerRepository.save(answer);
            return modelMapper.toAnswerResponse(answer);
        } catch (DataIntegrityViolationException e){
            throw new AlreadyExsistException(e.getMessage());
        }

    }
}
