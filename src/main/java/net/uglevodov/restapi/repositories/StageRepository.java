/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.CookingStage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StageRepository extends JpaRepository<CookingStage, Long> {
}
