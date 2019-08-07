/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.CookingStage;
import net.uglevodov.restapi.entities.CookingStages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CookingRepository extends JpaRepository<CookingStages, Long> {

    @Query("FROM CookingStages WHERE cookingStage = :containing")
    List<CookingStages> findAllByStagesContaining(CookingStage containing);
}
