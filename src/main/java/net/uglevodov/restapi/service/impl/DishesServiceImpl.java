package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.Dish;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.repositories.DishesRepository;
import net.uglevodov.restapi.service.DishesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DishesServiceImpl implements DishesService {


    @Autowired
    DishesRepository dishesRepository;


    @Override
    public Dish save(Dish entity) {
        return null;
    }

    @Override
    public Dish get(long id) throws NotFoundException {
        log.trace("[{}] - Getting dish id = ", this.getClass().getSimpleName(), id);

        return dishesRepository.findById(id).orElseThrow(()-> new NotFoundException("dish id " + id + " not found"));
    }

    @Override
    public void update(Dish entity) throws NotUpdatableException {

    }

    @Override
    public void delete(long id) throws NotFoundException {
        log.trace("[{}] - Deleting dish id = {}", this.getClass().getSimpleName(), id);
        Dish dish = dishesRepository.findById(id).orElseThrow(()-> new NotFoundException("dish id " + id + " not found"));

        dishesRepository.delete(dish);
    }

    @Override
    public Page<Dish> getAll(Pageable pageRequest) {
        return null;
    }
}
