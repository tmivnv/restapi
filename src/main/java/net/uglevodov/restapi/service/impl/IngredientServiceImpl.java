package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.Ingredient;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.repositories.IngredientRepository;
import net.uglevodov.restapi.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Slf4j
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    IngredientRepository repository;

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
    public void update(Ingredient ingredient) throws NotUpdatableException {
        log.trace("[{}] - Updating ingredient {}", this.getClass().getSimpleName(), ingredient);

        Assert.notNull(ingredient, "Ingredient can not be null");
        repository.save(ingredient);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        log.trace("[{}] - Deleting ingredient id = {}", this.getClass().getSimpleName(), id);
        Ingredient ingredient = repository.findById(id).orElseThrow(()-> new NotFoundException("ingredient id " + id + " not found"));

        repository.delete(ingredient);
    }

    @Override
    public List<Ingredient> getAll() {
        log.trace("[{}] - Getting ingredients list", this.getClass().getSimpleName());

        return repository.findAll(new Sort(Sort.Direction.ASC, "ingredientName"));
    }
}
