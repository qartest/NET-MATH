package ru.nsu.rush_c.services;

import ru.nsu.rush_c.payload.article.ArticleCreateRequest;
import ru.nsu.rush_c.payload.article.ArticleResponse;
import ru.nsu.rush_c.payload.module.ModuleCreateRequest;
import ru.nsu.rush_c.payload.module.ModuleResponse;

import java.util.List;

public interface ModuleService {

    List<ModuleResponse> getAllModule();

    ModuleResponse getModuleById(int id);

    ModuleResponse createModule(ModuleCreateRequest moduleCreateRequest);

    ModuleResponse deleteModule(int id);

    ModuleResponse updateModule(int id, ModuleCreateRequest moduleCreateRequest);
}
