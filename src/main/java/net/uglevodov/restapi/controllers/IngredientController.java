package net.uglevodov.restapi.controllers;

import net.uglevodov.restapi.dto.ApiResponse;
import net.uglevodov.restapi.entities.Ingredient;
import net.uglevodov.restapi.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/ingredients")
public class IngredientController {

    @Autowired
    IngredientService ingredientService;

    @GetMapping(value = "/get")
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id) {
        var ingredient = ingredientService.get(id);

        return new ResponseEntity<>(ingredient, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageRequest) {
        return new ResponseEntity<>(ingredientService.getAll(pageRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            @RequestBody Ingredient ingredient
    ) {

        return new ResponseEntity<>(ingredientService.save(ingredient), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @RequestBody Ingredient ingredient
            ) {
        ingredientService.update(ingredient);
        return new ResponseEntity<>(ingredient, HttpStatus.ACCEPTED);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> delete(@RequestParam(value = "id") Long id) {
        ingredientService.delete(id);

        return new ResponseEntity<>(new ApiResponse(true, "Ingredient deleted, id "+id), HttpStatus.OK);
    }
}
