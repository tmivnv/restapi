/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.Ingredient;
import net.uglevodov.restapi.entities.Recipe;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.repositories.DishesRepository;
import net.uglevodov.restapi.repositories.IngredientRepository;
import net.uglevodov.restapi.repositories.RecipeRepository;
import net.uglevodov.restapi.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Slf4j
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientRepository repository;
    @Autowired
    private RecipeRepository recipeRepository;

    @Override
    public Ingredient save(Ingredient ingredient) {
        log.trace("[{}] - Saving ingredient {}", this.getClass().getSimpleName(), ingredient);
        return repository.save(ingredient);
    }

    @Override
    public Ingredient get(long id) throws NotFoundException {
        log.trace("[{}] - Getting ingredient id = ", this.getClass().getSimpleName(), id);

        return repository.findById(id).orElseThrow(()-> new NotFoundException("ingredient id " + id + " not found"));
    }

    @Override
    public void update(Ingredient ingredient) throws NotUpdatableException, NotFoundException {
        log.trace("[{}] - Updating ingredient {}", this.getClass().getSimpleName(), ingredient);
        Ingredient oldIngredient = repository.findById(ingredient.getId()).orElseThrow(()-> new NotFoundException("ingredient id " + ingredient.getId() + " not found"));

        if (ingredient.getCarbs()==null) ingredient.setCarbs(oldIngredient.getCarbs());
        if (ingredient.getDescription()==null) ingredient.setDescription(oldIngredient.getDescription());

        if (ingredient.getImage()==null) ingredient.setImage(oldIngredient.getImage());
        if (ingredient.getImagePath()==null) ingredient.setImagePath(oldIngredient.getImagePath());
        if (ingredient.getIngredientName()==null) ingredient.setIngredientName(oldIngredient.getIngredientName());
        if (ingredient.getUglevodovnetGroup()==null) ingredient.setUglevodovnetGroup(oldIngredient.getUglevodovnetGroup());
        if (ingredient.getUnit()==null) ingredient.setUnit(oldIngredient.getUnit());
        if (ingredient.getUnitWeight()==null) ingredient.setUnitWeight(oldIngredient.getUnitWeight());


        repository.save(ingredient);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        log.trace("[{}] - Deleting ingredient id = {}", this.getClass().getSimpleName(), id);
        Ingredient ingredient = repository.findById(id).orElseThrow(()-> new NotFoundException("ingredient id " + id + " not found"));

        if (!recipeRepository.findAllByIngredientContaining(ingredient).isEmpty()) throw new NotUpdatableException("This ingredient is used somewhere and can not be deleted");

        repository.delete(ingredient);
    }

    @Override
    public Page<Ingredient> getAll(Pageable pageRequest) {
        log.trace("[{}] - Getting ingredients list", this.getClass().getSimpleName());

        return repository.findAll(pageRequest);
    }

    @Override
    public List<Ingredient> getAllByNameContaining(String name) {
        return name.isEmpty() ? null : repository.findAllByIngredientNameIsContainingIgnoreCase(name);
    }
}
