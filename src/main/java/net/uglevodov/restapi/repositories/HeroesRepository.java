/*
 * Copyright (c) 2020. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.Hero;
import net.uglevodov.restapi.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HeroesRepository extends JpaRepository<Hero, Long> {
}
