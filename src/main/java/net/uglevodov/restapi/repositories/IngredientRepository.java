package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findAllByIngredientNameIsContainingIgnoreCase(String name);
}
