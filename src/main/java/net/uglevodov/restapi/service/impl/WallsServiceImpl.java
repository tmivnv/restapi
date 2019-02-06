package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.Wall;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.exceptions.WrongOwnerException;
import net.uglevodov.restapi.repositories.WallsRepository;
import net.uglevodov.restapi.service.WallsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WallsServiceImpl implements WallsService {

    @Autowired
    WallsRepository wallsRepository;

    @Override
    public Wall save(Wall owned, long userId) throws WrongOwnerException {
        return null;
    }

    @Override
    public Wall get(long id) throws NotFoundException {
        log.trace("[{}] - Getting wall id = ", this.getClass().getSimpleName(), id);

        return wallsRepository.findById(id).orElseThrow(() -> new NotFoundException("wall id " + id + " not found"));
    }

    @Override
    public Wall getByUser(Long userId) throws NotFoundException {
        log.trace("[{}] - Getting wall of user id = ", this.getClass().getSimpleName(), userId);

        return wallsRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("user id " + userId + " not found"));
    }

    @Override
    public void update(Wall owned, long userId) throws NotUpdatableException, WrongOwnerException {

    }

    @Override
    public void delete(long id, long userId) throws NotFoundException {

    }

    @Override
    public void delete(long id) throws NotFoundException {

    }

    @Override
    public Page<Wall> getAll(Pageable pageRequest) {
        return null;
    }
}
