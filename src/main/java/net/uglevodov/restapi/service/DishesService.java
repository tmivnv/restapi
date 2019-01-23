package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.Dish;
import net.uglevodov.restapi.entities.Ingredient;

import java.util.List;

public interface DishesService extends GenericService<Dish> {
    List<Dish> findAllByIngredientsContaining(Ingredient ingredient);
}
