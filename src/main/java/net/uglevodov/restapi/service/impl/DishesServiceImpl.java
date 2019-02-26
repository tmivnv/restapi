/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.*;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.repositories.DishesRepository;
import net.uglevodov.restapi.repositories.RecipeRepository;
import net.uglevodov.restapi.repositories.UserRepository;
import net.uglevodov.restapi.service.DishesService;
import net.uglevodov.restapi.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishesServiceImpl implements DishesService {


    @Autowired
    private DishesRepository dishesRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventService eventService;


    @Override
    public Dish save(Dish dish) {
        log.trace("[{}] - Saving dish {}", this.getClass().getSimpleName(), dish);
        return dishesRepository.save(dish);
    }

    @Override
    public Dish get(long id) throws NotFoundException {
        log.trace("[{}] - Getting dish id = ", this.getClass().getSimpleName(), id);

        return dishesRepository.findById(id).orElseThrow(() -> new NotFoundException("dish id " + id + " not found"));
    }

    @Override
    public void update(Dish dish) throws NotUpdatableException, NotFoundException {
        log.trace("[{}] - Updating dish {}", this.getClass().getSimpleName(), dish);
        dishesRepository.findById(dish.getId()).orElseThrow(() -> new NotFoundException("dish id " + dish.getId() + " not found"));
        Assert.notNull(dish, "Dish can not be null");
        dishesRepository.save(dish);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        log.trace("[{}] - Deleting dish id = {}", this.getClass().getSimpleName(), id);
        Dish dish = dishesRepository.findById(id).orElseThrow(() -> new NotFoundException("dish id " + id + " not found"));

        dishesRepository.delete(dish);
    }

    @Override
    public Page<Dish> getAll(Pageable pageRequest) {
        log.trace("[{}] - Getting dish list", this.getClass().getSimpleName());

        return dishesRepository.findAll(pageRequest);
    }

    @Override
    public Set<Dish> findAllByIngredientsContainingAndNotContaining(List<Ingredient> containing, List<Ingredient> notContaining, String name) {
        List<Recipe> containingRecipes = new ArrayList<>();


        for (Ingredient ingredient : containing) {
            containingRecipes.addAll(recipeRepository.findAllByIngredientContaining(ingredient));
        }


        List<Recipe> notContainingRecipes = new ArrayList<>();

        for (Ingredient ingredient : notContaining) {
            notContainingRecipes.addAll(recipeRepository.findAllByIngredientContaining(ingredient));
        }


        Set<Dish> dishesFound = new HashSet<>();

        if (!containingRecipes.isEmpty()) {
            for (Recipe recipe : containingRecipes) {
                dishesFound.addAll(dishesRepository.findAllByIngredientsContaining(recipe));
            }
        } else dishesFound.addAll(dishesRepository.findAll());

        for (Recipe recipe : notContainingRecipes) {
            dishesFound.removeAll(dishesRepository.findAllByIngredientsContaining(recipe));
        }

        dishesFound = dishesFound.stream().filter(d -> d.getDishName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toSet());

        if (dishesFound.size() == 0) throw new NotFoundException("no dishes found");

        return dishesFound;
    }

    @Override
    public Dish favorUnfavor(Long userId, Long dishId) {
        FavoredByUser favoredByUser = new FavoredByUser();
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user id " + userId + " not found"));
        favoredByUser.setUserId(userId);
        favoredByUser.setDishId(dishId);
        favoredByUser.setCreated(LocalDateTime.now());

        Dish dish = dishesRepository.findById(dishId).orElseThrow(() -> new NotFoundException("dish id " + dishId + " not found"));
        FavoredByUser alreadyFavored = dish.getFavoredByUsers().stream().filter(l -> l.getUserId().equals(userId)).findFirst().orElse(null);

        if (alreadyFavored != null) {
            dish.getFavoredByUsers().remove(alreadyFavored);
        } else {
            dish.getFavoredByUsers().add(favoredByUser);

            //рассказываем фолловерам об этом событии
            for (Follower follower : user.getFollowers()) {

                User followerUser = userRepository.findById(follower.getFollowerId()).orElseThrow(() -> new NotFoundException("user id " + follower.getFollowerId() + " not found"));
                if (followerUser.isFollowingFavor()) { //если фолловер хочет видеть этот тип события
                    Event event = new Event();
                    event.setRead(false);
                    event.setCreated(LocalDateTime.now());
                    event.setLink("/api/dishes/get?id=" + dish.getId());
                    event.setType("favor");
                    event.setUser(followerUser);
                    event.setMessage(user.getFirstName() + " " + user.getLastName() + (user.isWoman() ? " добавила" : " добавил") + " в избранное блюдо " + dish.getDishName());
                    if (eventService.findByMessageContainingIgnoreCase(event.getMessage())==null) //если еще нет такого события
                    eventService.save(event);
                }
            }
        }


        return dishesRepository.saveAndFlush(dish);
    }
}
