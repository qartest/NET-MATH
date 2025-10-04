package ru.nsu.rush_c.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.nsu.rush_c.dao.repository.ModuleRepository;
import ru.nsu.rush_c.error.exception.AlreadyExsistException;
import ru.nsu.rush_c.error.exception.NotFoundException;
import ru.nsu.rush_c.models.mapper.ModelMapper;
import ru.nsu.rush_c.payload.module.ModuleCreateRequest;
import ru.nsu.rush_c.payload.module.ModuleResponse;
import ru.nsu.rush_c.services.ModuleService;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public List<ModuleResponse> getAllModule() {
        List<ru. nsu. rush_c. models. Module> modules = moduleRepository.findAll();
        return modules.stream().map(module -> modelMapper.toModuleResponse(module)).collect(Collectors.toList());
    }

    @Transactional
    public ModuleResponse getModuleById(int id) {
        ru.nsu.rush_c.models.Module module = moduleRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found when found by id", id));
        return modelMapper.toModuleResponse(module);
    }

    @Transactional
    public ModuleResponse createModule(ModuleCreateRequest moduleCreateRequest) {
        ru.nsu.rush_c.models.Module module = modelMapper.toModule(moduleCreateRequest);

        module.setData_update(null);
        module.setData_create(ZonedDateTime.now());

        try{
            module = moduleRepository.save(module);
            return  modelMapper.toModuleResponse(module);

        } catch (DataIntegrityViolationException e){
            throw new AlreadyExsistException(e.getMessage());
        }
    }

    @Transactional
    public ModuleResponse deleteModule(int id) {
        ModuleResponse moduleResponse;
        if(moduleRepository.existsById(id)){
            moduleResponse = modelMapper.toModuleResponse(moduleRepository.findById(id).orElse(null));
            moduleRepository.deleteById(id);
        }
        else{
            throw new NotFoundException("Not found", id);
        }

        return moduleResponse;
    }

    @Transactional
    public ModuleResponse updateModule(int id, ModuleCreateRequest moduleCreateRequest) {
        ru.nsu.rush_c.models.Module module = moduleRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found when found by id", id));

        module.setName(moduleCreateRequest.getName());
        module.setDescription(moduleCreateRequest.getDescription());
        module.setData_update(ZonedDateTime.now());

        try{
            module = moduleRepository.save(module);
            return  modelMapper.toModuleResponse(module);

        } catch (DataIntegrityViolationException e){
            throw new AlreadyExsistException(e.getMessage());
        }

    }
}
