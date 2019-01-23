package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DishesRepository extends JpaRepository<Dish, Long> {

}
