/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.controllers;

import io.swagger.annotations.*;
import net.uglevodov.restapi.dto.*;
import net.uglevodov.restapi.dto.ApiResponse;
import net.uglevodov.restapi.entities.*;
import net.uglevodov.restapi.security.UserPrincipal;
import net.uglevodov.restapi.service.CookingService;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
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

    @Autowired
    CookingService cookingService;




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



    @GetMapping(value = "/get-categories")
    @ApiOperation(
            value = "Получить категории блюд",
            notes = "Получить категории блюд",
            response = DishCategoryDto.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Блюдо не найдено" )

    } )
    public ResponseEntity<?> getCategories(@RequestParam(value = "number") Long number) {

        String[] types =  {"Завтраки","Супы","Салаты","Закуски","Десерты","Запеканки и омлеты","Второе","Гарниры","Перекусы"};
        List<DishCategoryDto> categories = new ArrayList<>();
        for (int i=1; i<10; i++)
        {
            DishCategoryDto dishCategoryDto = new DishCategoryDto();
            dishCategoryDto.setId((long)i);
            dishCategoryDto.setCategoryName(types[i-1]);
            dishCategoryDto.setDishesNumber(dishesService.categoryNumber(i));
            dishCategoryDto.setDishes(dishesService.categoryDishes(i, number.intValue()));



            categories.add(dishCategoryDto);
        }

        return new ResponseEntity<>(categories.stream()
                .sorted(Comparator.comparing(DishCategoryDto::getId))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/calculate")
    @ApiOperation(
            value = "Рассчитать углеводы и группу",
            notes = "Рассчитать углеводы и группу",
            response = Dish.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Блюдо не найдено" )

    } )
    public ResponseEntity<?> calculate(@RequestParam(value = "id") Long id) {
        var dish = dishesService.get(id);
        Double carbs = 0D;
        Long totalWeight = 0L;

        for (IngredientGroup group : dish.getIngredientGroups()) {

            Set<Recipe> ingredients = group.getGroup();


            for (Recipe ing : ingredients)
            {
                carbs += ing.getIngredient().getCarbs()*ing.getWeight()*ing.getIngredient().getUnitWeight();
                totalWeight += ing.getWeight()*ing.getIngredient().getUnitWeight();
            }
        }


        carbs = carbs/totalWeight;

        int group;

        if (carbs<2) group = 1; else
            if (carbs<3.5) group = 2; else
                group = 3;

        BigDecimal bd = new BigDecimal(Double.toString(carbs));
        bd = bd.setScale(1, RoundingMode.HALF_UP);

        dish.setCarbs(bd.doubleValue());

        dish.setUglevodovnetGroup(group);

        return new ResponseEntity<>(dishesService.save(dish), HttpStatus.OK);
    }



  /*  @ApiOperation(
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
*/

    @ApiOperation(
            value = "Добавить этап приготовления",
            notes = "Добавить этап приготовления",
            response = Dish.class
    )
    @ApiResponses( {
            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Ни одного блюда не найдено" )
    } )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "true" )
    @PostMapping(value = "/addstage", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addStage(
            @RequestBody StageDto stageDto,
            @RequestParam(value = "id") Long id
    ) {

        CookingStages stage = new CookingStages();
        CookingStage s = new CookingStage();
        s.setDescription(stageDto.getDescription());
        s.setImage(stageDto.getImage());
        s.setImagePath(stageDto.getImagePath());

        stagesService.save(s);

        stage.setCookingStage(s);
        stage.setStageNumber(stageDto.getStageNumber());

        cookingService.save(stage);

        Dish dish = dishesService.get(id);
        dish.getStages().add(stage);


        return new ResponseEntity<>(dishesService.save(dish), HttpStatus.OK);
    }


    @ApiOperation(
            value = "Обновить этап приготовления",
            notes = "Обновить этап приготовления",
            response = Dish.class
    )
    @ApiResponses( {
            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Ни одного блюда не найдено" )
    } )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
    @PutMapping(value = "/updateStage", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateStage(
            @RequestBody StageDto stageDto,
            @RequestParam(value = "id") Long id
    ) {

        CookingStages stage = cookingService.get(id);
        CookingStage s = stage.getCookingStage();
        if (stageDto.getDescription()!=null) s.setDescription(stageDto.getDescription());
        if (stageDto.getImage()!=null) s.setImage(stageDto.getImage());
        if (stageDto.getImagePath()!=null) s.setImagePath(stageDto.getImagePath());

        stagesService.save(s);

        stage.setCookingStage(s);
        if (stageDto.getStageNumber()!=null) stage.setStageNumber(stageDto.getStageNumber());

        return new ResponseEntity<>(cookingService.save(stage), HttpStatus.OK);
    }


    @ApiOperation(
            value = "Удалить этап приготовления",
            notes = "Удалить этап приготовления",
            response = Dish.class
    )
    @ApiResponses( {
            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Ни одного блюда не найдено" )
    } )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
    @DeleteMapping(value = "/deleteStage")
    public ResponseEntity<?> deleteStage(
            @RequestParam(value = "stageId") Long stageId,
            @RequestParam(value = "dishId") Long dishId
    ) {

        CookingStages stage = cookingService.get(stageId);

        Dish dish = dishesService.get(dishId);

        dish.getStages().remove(stage);
        dishesService.save(dish);

        return new ResponseEntity<>(dishesService.get(dishId), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Создание нового блюда",
            notes = "Создание нового блюда",
            response = Dish.class
    )
    @ApiResponses( {
            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" )
    } )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            @RequestBody DishDto dishDto
    ) {
        Set<IngredientGroup> ingredientGroups = new HashSet<>();
        if (dishDto.getIngredientGroups()!=null)

            for (IngredientGroupDto group : dishDto.getIngredientGroups())
            {
                Set<Recipe> ingredients = new HashSet<>();
                for (DishIngredientsDto entry : group.getIngredients())
                {
                    ingredients.add(new Recipe(ingredientService.get(entry.getId()), entry.getWeight()));
                }

                ingredientGroups.add(new IngredientGroup(ingredients,group.getName()));
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
                ingredientGroups,
                stages,
                dishDto.getType(),
                dishDto.getTypeNumber(),
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
    @CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
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
        if (dishDto.getIngredientGroups()!=null) {
            dish.getIngredientGroups().clear();

            for (IngredientGroupDto group : dishDto.getIngredientGroups() )
            {
                IngredientGroup ingredientGroup = new IngredientGroup();
                ingredientGroup.setGroup(new HashSet<Recipe>());
                ingredientGroup.setName(group.getName());

                if (group.getIngredients()!=null)
                for (DishIngredientsDto entry : group.getIngredients()) {
                    ingredientGroup.getGroup().add(new Recipe(ingredientService.get(entry.getId()), entry.getWeight()));

                }
                dish.getIngredientGroups().add(ingredientGroup);
            }

        }
        if (dishDto.getStages()!=null) {
            dish.getStages().clear();

            for (DishStagesDto entry : dishDto.getStages()) {
                dish.getStages().add(new CookingStages(stagesService.get(entry.getId()), entry.getNumber()));
            }
        }

        if (dishDto.getPortion()!=null) dish.setPortion(dishDto.getPortion());
        if (dishDto.getTypeNumber()!=null) dish.setTypeNumber(dishDto.getTypeNumber());
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
            value = "Получить все блюда категории",
            notes = "Получить все блюда категории (постранично)",
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
    @GetMapping(value = "/get-category")
    public ResponseEntity<?> getAllCategory(Long categoryId, Pageable pageRequest) {
        return new ResponseEntity<>(dishesService.getAllCategory(categoryId, pageRequest), HttpStatus.OK);
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
