/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.controllers;

import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import net.uglevodov.restapi.dto.ApiResponse;
import net.uglevodov.restapi.dto.IngredientDto;
import net.uglevodov.restapi.entities.Ingredient;
import net.uglevodov.restapi.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/ingredients")
@Api( value = "/api/ingredients", description = "Контроллер ингредиентов" )

public class IngredientController {

    @Autowired
    IngredientService ingredientService;

    @ApiOperation(
            value = "Получить ингредиент",
            notes = "Получить ингредиент по айди",
            response = Ingredient.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Ингредиент не найден" )

    } )
    @GetMapping(value = "/get")
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id) {
        var ingredient = ingredientService.get(id);

        return new ResponseEntity<>(ingredient, HttpStatus.OK);
    }




    @ApiOperation(
            value = "Получить все ингредиенты",
            notes = "Получить все ингредиенты",
            response = Page.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" )

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
        return new ResponseEntity<>(ingredientService.getAll(pageRequest), HttpStatus.OK);
    }



    @ApiOperation(
            value = "Получить ингредиент по названию",
            notes = "Получить ингредиент по названию (список ингредиентов, в названии которых есть name, case insensitive)",
            response = Ingredient.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Ингредиенты не найдены" )

    } )
    @GetMapping(value = "findbyname")
    public ResponseEntity<?> findByName(@RequestParam(value = "name") String name) {
        return new ResponseEntity<>(ingredientService.getAllByNameContaining(name), HttpStatus.OK);
    }


    @ApiOperation(
            value = "Добавить новый ингредиент (требуются права админа)",
            notes = "Добавить новый ингредиент (требуются права админа)",
            response = Ingredient.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" )

    } )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            @RequestBody IngredientDto ingredientDto
    ) {

        return new ResponseEntity<>(ingredientService.save(new Ingredient(ingredientDto)), HttpStatus.OK);
    }




    @ApiOperation(
            value = "Изменить ингредиент (требуются права админа)",
            notes = "Изменить ингредиент (требуются права админа)",
            response = Ingredient.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Ингредиент не найдены" )

    } )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @RequestBody IngredientDto ingredientDto,
            @RequestParam(value = "id") Long id
            ) {
        Ingredient ingredient = new Ingredient(ingredientDto);
        ingredient.setId(id);
        ingredientService.update(ingredient);
        return new ResponseEntity<>(ingredient, HttpStatus.OK);
    }


    @ApiOperation(
            value = "Удалить ингредиент (требуются права админа)",
            notes = "Удалить ингредиент (требуются права админа)",
            response = Ingredient.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Ингредиент не найден" ),
            @io.swagger.annotations.ApiResponse( code = 409, message = "Ингредиент используется в блюде, удаление невозможно" )

    } )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/delete")
    @CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
    public ResponseEntity<?> delete(@RequestParam(value = "id") Long id) {
        ingredientService.delete(id);

        return new ResponseEntity<>(new ApiResponse(true, "Ingredient deleted, id "+id), HttpStatus.OK);
    }
}
