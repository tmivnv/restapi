package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.Dish;
import net.uglevodov.restapi.entities.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface DishesService extends GenericService<Dish> {
   // Set<Dish> findAllByIngredientsContainingAndNotContaining(List<Ingredient> containing, List<Ingredient> notContaining, String name);
    Dish favorUnfavor(Long userId, Long dishId);
    Set<Dish> categoryDishes(int categoryId, int size);
    Long categoryNumber(int categoryId);
    Page<Dish> getAllCategory(Long categoryId, Pageable pageRequest);
}
