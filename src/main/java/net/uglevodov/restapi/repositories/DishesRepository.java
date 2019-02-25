package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.Dish;
import net.uglevodov.restapi.entities.Ingredient;
import net.uglevodov.restapi.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DishesRepository extends JpaRepository<Dish, Long> {

    List<Dish> findAllByIngredientsContaining(Recipe recipe);




}