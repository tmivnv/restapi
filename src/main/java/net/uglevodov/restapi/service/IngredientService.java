package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.Ingredient;

import java.util.List;

public interface IngredientService extends  GenericService<Ingredient> {

    List<Ingredient> getAllByNameContaining(String name);
}
