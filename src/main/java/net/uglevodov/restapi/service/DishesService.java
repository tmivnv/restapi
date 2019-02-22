package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.Dish;
import net.uglevodov.restapi.entities.Ingredient;

import java.util.List;
import java.util.Set;

public interface DishesService extends GenericService<Dish> {
    Set<Dish> findAllByIngredientsContainingAndNotContaining(List<Ingredient> containing, List<Ingredient> notContaining, String name);
    Dish favorUnfavor(Long userId, Long dishId);
}
