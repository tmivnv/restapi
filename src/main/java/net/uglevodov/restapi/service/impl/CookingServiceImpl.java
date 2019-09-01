/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.CookingStages;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.repositories.CookingRepository;
import net.uglevodov.restapi.service.CookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CookingServiceImpl implements CookingService {

    @Autowired
    CookingRepository cookingRepository;

    @Override
    public CookingStages save(CookingStages entity) {
        return cookingRepository.save(entity);
    }

    @Override
    public CookingStages get(long id) throws NotFoundException {
        return cookingRepository.findById(id).orElseThrow(() -> new NotFoundException("stage id " + id + " not found"));

    }

    @Override
    public void update(CookingStages entity) throws NotUpdatableException {

    }

    @Override
    public void delete(long id) throws NotFoundException {
        CookingStages stages = cookingRepository.findById(id).orElseThrow(() -> new NotFoundException("stage id " + id + " not found"));

        cookingRepository.delete(stages);
    }

    @Override
    public Page<CookingStages> getAll(Pageable pageRequest) {
        return null;
    }
}
