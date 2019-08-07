/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.controllers;

import io.swagger.annotations.*;
import net.uglevodov.restapi.dto.*;
import net.uglevodov.restapi.dto.ApiResponse;
import net.uglevodov.restapi.entities.CookingStages;
import net.uglevodov.restapi.entities.Dish;
import net.uglevodov.restapi.entities.Ingredient;
import net.uglevodov.restapi.entities.Recipe;
import net.uglevodov.restapi.security.UserPrincipal;
import net.uglevodov.restapi.service.DishesService;
import net.uglevodov.restapi.service.IngredientService;
import net.uglevodov.restapi.service.StagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Autowired
    StagesService stagesService;




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
        if (dishDto.getIngredients()!=null)
        for (DishIngredientsDto entry : dishDto.getIngredients())
        {
            ingredients.add(new Recipe(ingredientService.get(entry.getId()), entry.getWeight()));
        }

        Set<CookingStages> stages = new HashSet<>();
        if (dishDto.getStages()!=null)
            for (DishStagesDto entry : dishDto.getStages())
            {
                stages.add(new CookingStages(stagesService.get(entry.getId()), entry.getNumber()));
            }

        Dish dish = new Dish(dishDto.getDishName(),
                dishDto.getDescription(),
                dishDto.getImage(),
                dishDto.getImagePath(),
                dishDto.getUglevodovnetGroup(),
                dishDto.getCarbs(),
                dishDto.getPortion(),
                dishDto.getActive(),
                ingredients,
                stages,
                dishDto.getType(),
                null
                );

        return new ResponseEntity<>(dishesService.save(dish), HttpStatus.OK);
    }




    @ApiOperation(
            value = "Удаление блюда",
            notes = "Удаление блюда по айди (требуются права админа)",
            response = ApiResponse.class
    )
    @ApiResponses( {
            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Блюдо не найдено" )
    } )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> delete(@RequestParam(value = "id") Long id) {
        dishesService.delete(id);

        return new ResponseEntity<>(new ApiResponse(true, "Dish deleted, id "+id), HttpStatus.OK);
    }




    @ApiOperation(
            value = "Изменение блюда",
            notes = "Изменение блюда по айди (требуются права админа)",
            response = Dish.class
    )
    @ApiResponses( {
            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Блюдо не найдено" )
    } )
    @CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestParam(value = "id") Long id,
            @RequestBody DishDto dishDto
    ) {
        Dish dish = dishesService.get(id);
        if (dishDto.getActive()!=null) dish.setActive(dishDto.getActive());
        if (dishDto.getCarbs()!=null) dish.setCarbs(dishDto.getCarbs());
        if (dishDto.getDescription()!=null) dish.setDescription(dishDto.getDescription());
        if (dishDto.getDishName()!=null) dish.setDishName(dishDto.getDishName());
        if (dishDto.getImage()!=null) dish.setImage(dishDto.getImage());
        if (dishDto.getImagePath()!=null) dish.setImagePath(dishDto.getImagePath());
        if (dishDto.getIngredients()!=null) {
            dish.getIngredients().clear();

            for (DishIngredientsDto entry : dishDto.getIngredients()) {
                dish.getIngredients().add(new Recipe(ingredientService.get(entry.getId()), entry.getWeight()));
            }
        }
        if (dishDto.getStages()!=null) {
            dish.getStages().clear();

            for (DishStagesDto entry : dishDto.getStages()) {
                dish.getStages().add(new CookingStages(stagesService.get(entry.getId()), entry.getNumber()));
            }
        }

        if (dishDto.getPortion()!=null) dish.setPortion(dishDto.getPortion());
        if (dishDto.getType()!=null) dish.setType(dishDto.getType());
        if (dishDto.getUglevodovnetGroup()!=null) dish.setUglevodovnetGroup(dishDto.getUglevodovnetGroup());

        dishesService.update(dish);
        return new ResponseEntity<>(dishesService.get(id), HttpStatus.OK);
    }




    @ApiOperation(
            value = "Получить все блюда",
            notes = "Получить все блюда (постранично)",
            response = Page.class
    )
    @ApiResponses( {
            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Блюдо не найдено" )
    } )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageRequest) {
        return new ResponseEntity<>(dishesService.getAll(pageRequest), HttpStatus.OK);
    }





    @ApiOperation(
            value = "Добавить в/убрать из избранного",
            notes = "Добавляет в избранное, если уже в избранном - убирает",
            response = Dish.class
    )
    @ApiResponses( {
            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Блюдо не найдено" )
    } )
    @GetMapping(value = "/favor-unfavor")
    public ResponseEntity<?> favorUnfavor(
            @RequestParam(value = "id") Long id,
            @AuthenticationPrincipal UserPrincipal principal
    )
    {
        return new ResponseEntity<>(dishesService.favorUnfavor(principal.getId(), id), HttpStatus.OK);
    }
}
