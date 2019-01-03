package net.uglevodov.restapi.controllers;

import net.uglevodov.restapi.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(ingredientService.getAll(), HttpStatus.OK);
    }

    //TODO: дописать
}
