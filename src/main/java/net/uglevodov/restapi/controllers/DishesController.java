package net.uglevodov.restapi.controllers;

import net.uglevodov.restapi.dto.ApiResponse;
import net.uglevodov.restapi.dto.DishFilterDto;
import net.uglevodov.restapi.entities.Dish;
import net.uglevodov.restapi.entities.Ingredient;
import net.uglevodov.restapi.security.UserPrincipal;
import net.uglevodov.restapi.service.DishesService;
import net.uglevodov.restapi.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/dishes")
public class DishesController {

    @Autowired
    DishesService dishesService;

    @Autowired
    IngredientService ingredientService;

    @GetMapping(value = "/get")
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id) {
        var dish = dishesService.get(id);

        return new ResponseEntity<>(dish, HttpStatus.OK);
    }

    @PostMapping(value = "/filter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> filter(
            @RequestBody DishFilterDto dishFilterDto
            ) {
        List<Ingredient> included = new ArrayList<>();
        List<Ingredient> excluded = new ArrayList<>();

        for (Long ingredient : dishFilterDto.getIncludeIngredients())
        {
            included.add(ingredientService.get(ingredient));
        }

        for (Long ingredient : dishFilterDto.getExcludeIngredients())
        {
            excluded.add(ingredientService.get(ingredient));
        }

        Set<Dish> dishesFound = dishesService.findAllByIngredientsContainingAndNotContaining(included, excluded);


        dishesFound = dishesFound.stream().filter(d -> d.getDishName().toLowerCase().contains(dishFilterDto.getName().toLowerCase())).collect(Collectors.toSet());

        return new ResponseEntity<>(dishesFound, HttpStatus.ACCEPTED);
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @RequestBody Dish dish
    ) {
        dishesService.get(dish.getId());
        dishesService.update(dish);
        return new ResponseEntity<>(new ApiResponse(true, "Dish updated, id "+dish.getId()), HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageRequest) {
        return new ResponseEntity<>(dishesService.getAll(pageRequest), HttpStatus.OK);
    }


    @GetMapping(value = "/favor-unfavor")
    public ResponseEntity<?> favorUnfavor(
            @RequestParam(value = "id") Long id,
            @AuthenticationPrincipal UserPrincipal principal
    )
    {
        return new ResponseEntity<>(dishesService.favorUnfavor(principal.getId(), id), HttpStatus.OK);
    }
}
