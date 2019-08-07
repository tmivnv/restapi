/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.service.impl;


import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.CookingStage;
import net.uglevodov.restapi.entities.CookingStages;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.repositories.CookingRepository;
import net.uglevodov.restapi.repositories.IngredientRepository;
import net.uglevodov.restapi.repositories.StageRepository;
import net.uglevodov.restapi.service.GenericService;
import net.uglevodov.restapi.service.StagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StagesServiceImpl implements StagesService {

    @Autowired
    private StageRepository repository;

    @Autowired
    private CookingRepository cookingRepository;

    @Override
    public CookingStage save(CookingStage entity) {
        return repository.save(entity);
    }

    @Override
    public CookingStage get(long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(()-> new NotFoundException("stage id " + id + " not found"));
    }

    @Override
    public void update(CookingStage entity) throws NotUpdatableException {
        CookingStage oldStage = repository.findById(entity.getId()).orElseThrow(()-> new NotFoundException("stage id " + entity.getId() + " not found"));

        if (entity.getDescription()==null) entity.setDescription(oldStage.getDescription());
        if (entity.getImage()==null) entity.setImage(oldStage.getImage());
        if (entity.getImagePath()==null) entity.setImagePath(oldStage.getImagePath());



        repository.save(entity);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        CookingStage stage = repository.findById(id).orElseThrow(()-> new NotFoundException("stage id " + id + " not found"));

        if (!cookingRepository.findAllByStagesContaining(stage).isEmpty()) throw new NotUpdatableException("This stage is used somewhere and can not be deleted");

        repository.delete(stage);
    }

    @Override
    public Page<CookingStage> getAll(Pageable pageRequest) {
        return repository.findAll(pageRequest);
    }
}
