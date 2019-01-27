package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.Ingredient;
import net.uglevodov.restapi.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("FROM Recipe WHERE ingredient = :containing")
    List<Recipe> findAllByIngredientContaining(Ingredient containing);


}
