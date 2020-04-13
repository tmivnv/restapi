package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.Dish;
import net.uglevodov.restapi.entities.Ingredient;
import net.uglevodov.restapi.entities.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface DishesRepository extends JpaRepository<Dish, Long> {

   // List<Dish> findAllByIngredientsContaining(Recipe recipe);

   @Query("from Dish where typeNumber=:categoryId")
   List<Dish> categoryDishes(int categoryId, Pageable pageable);

   @Query("select count(id) from Dish where typeNumber=:categoryId")
   Long categoryNumber(int categoryId);


   Page<Dish> findAllByTypeNumber(int typeNumber, Pageable pageRequest);




}