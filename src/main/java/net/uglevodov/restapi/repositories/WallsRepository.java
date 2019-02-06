package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.Wall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WallsRepository extends JpaRepository<Wall, Long> {
    Optional<Wall> findByUserId(Long userId);
}
