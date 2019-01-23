package net.uglevodov.restapi.controllers;

import net.uglevodov.restapi.dto.ApiResponse;
import net.uglevodov.restapi.entities.Dish;
import net.uglevodov.restapi.service.DishesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/dishes")
public class DishesController {

    @Autowired
    DishesService dishesService;

    @GetMapping(value = "/get")
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id) {
        var dish = dishesService.get(id);

        return new ResponseEntity<>(dish, HttpStatus.OK);
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            @RequestBody Dish dish
    ) {
        var saved = dishesService.save(dish);
        return new ResponseEntity<>(new ApiResponse(true, "Dish created, id "+ saved.getId()), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> delete(@RequestParam(value = "id") Long id) {
        dishesService.delete(id);

        return new ResponseEntity<>(new ApiResponse(true, "Dish deleted, id "+id), HttpStatus.OK);
    }
}
