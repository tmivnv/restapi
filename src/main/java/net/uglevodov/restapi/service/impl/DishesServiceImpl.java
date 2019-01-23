package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.Dish;
import net.uglevodov.restapi.entities.Ingredient;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.repositories.DishesRepository;
import net.uglevodov.restapi.service.DishesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Slf4j
public class DishesServiceImpl implements DishesService {


    @Autowired
    DishesRepository dishesRepository;


    @Override
    public Dish save(Dish dish) {
        log.trace("[{}] - Saving dish {}", this.getClass().getSimpleName(), dish);
        return dishesRepository.save(dish);
    }

    @Override
    public Dish get(long id) throws NotFoundException {
        log.trace("[{}] - Getting dish id = ", this.getClass().getSimpleName(), id);

        return dishesRepository.findById(id).orElseThrow(()-> new NotFoundException("dish id " + id + " not found"));
    }

    @Override
    public void update(Dish dish) throws NotUpdatableException, NotFoundException {
        log.trace("[{}] - Updating dish {}", this.getClass().getSimpleName(), dish);
        dishesRepository.findById(dish.getId()).orElseThrow(()-> new NotFoundException("dish id " + dish.getId() + " not found"));
        Assert.notNull(dish, "Dish can not be null");
        dishesRepository.save(dish);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        log.trace("[{}] - Deleting dish id = {}", this.getClass().getSimpleName(), id);
        Dish dish = dishesRepository.findById(id).orElseThrow(()-> new NotFoundException("dish id " + id + " not found"));

        dishesRepository.delete(dish);
    }

    @Override
    public Page<Dish> getAll(Pageable pageRequest) {
        log.trace("[{}] - Getting dish list", this.getClass().getSimpleName());

        return dishesRepository.findAll(pageRequest);
    }

    @Override
    public List<Dish> findAllByIngredientsContaining(Ingredient ingredient) {
        return dishesRepository.findAllByIngredientsContaining(ingredient);
    }
}
