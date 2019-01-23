package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.Dish;
import net.uglevodov.restapi.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DishesRepository extends JpaRepository<Dish, Long> {

    List<Dish> findAllByIngredientsContaining(Ingredient ingredient);
}
