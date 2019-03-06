/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.StaticPage;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.repositories.StaticPageRepository;
import net.uglevodov.restapi.service.StaticPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StaticPageServiceImpl implements StaticPageService {

    @Autowired
    private StaticPageRepository staticPageRepository;

    @Override
    public StaticPage save(StaticPage entity) {
        return null;
    }

    @Override
    public StaticPage get(long id) throws NotFoundException {
        log.trace("[{}] - Getting page id = ", this.getClass().getSimpleName(), id);

        return staticPageRepository.findById(id).orElseThrow(()-> new NotFoundException("page not found"));
    }

    @Override
    public void update(StaticPage entity) throws NotUpdatableException {

    }

    @Override
    public void delete(long id) throws NotFoundException {

    }

    @Override
    public Page<StaticPage> getAll(Pageable pageRequest) {
        return null;
    }
}
