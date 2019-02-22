/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import net.uglevodov.restapi.dto.ApiResponse;
import net.uglevodov.restapi.dto.DishDto;
import net.uglevodov.restapi.dto.DishFilterDto;
import net.uglevodov.restapi.entities.Dish;
import net.uglevodov.restapi.entities.Ingredient;
import net.uglevodov.restapi.entities.Recipe;
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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/dishes")
@Api( value = "/api/dishes", description = "Контроллер блюд" )
public class DishesController {

    @Autowired
    DishesService dishesService;

    @Autowired
    IngredientService ingredientService;

    @GetMapping(value = "/get")
    @ApiOperation(
            value = "Получить блюдо по айди",
            notes = "Получить блюдо по айди",
            response = Dish.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Блюдо не найдено" )

    } )
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id) {
        var dish = dishesService.get(id);

        return new ResponseEntity<>(dish, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Фильтр/поиск блюд",
            notes = "Получить блюдо, содержащее includeIngredients, не содержащие excludeIngredients и имеющее в названии name (case insensitive)",
            response = Dish.class
    )
    @ApiResponses( {
            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Ни одного блюда не найдено" )
    } )
    @PostMapping(value = "/filter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> filter(
            @RequestBody DishFilterDto dishFilterDto
            ) {
        List<Ingredient> included = dishFilterDto.getIncludeIngredients().stream().map(i->ingredientService.get(i)).collect(Collectors.toList());
        List<Ingredient> excluded = dishFilterDto.getExcludeIngredients().stream().map(i->ingredientService.get(i)).collect(Collectors.toList());

        Set<Dish> dishesFound = dishesService.findAllByIngredientsContainingAndNotContaining(included, excluded, dishFilterDto.getName());

        return new ResponseEntity<>(dishesFound, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Создание нового блюда",
            notes = "Создание нового блюда",
            response = Dish.class
    )
    @ApiResponses( {
            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" )
    } )
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            @RequestBody DishDto dishDto
    ) {
        Set<Recipe> ingredients = new HashSet<>();
        for (Map.Entry<Long, Long> entry : dishDto.getIngredients().entrySet())
        {
            ingredients.add(new Recipe(ingredientService.get(entry.getKey()), entry.getValue()));
        }

        Dish dish = new Dish(dishDto.getDishName(),
                dishDto.getDescription(),
                dishDto.getImage(),
                dishDto.getUglevodovnetGroup(),
                dishDto.getCarbs(),
                dishDto.getPortion(),
                dishDto.getActive(),
                ingredients,
                dishDto.getType(),
                null
                );

        return new ResponseEntity<>(dishesService.save(dish), HttpStatus.OK);
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
