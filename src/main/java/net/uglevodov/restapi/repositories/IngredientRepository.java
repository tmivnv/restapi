package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
