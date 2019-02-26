package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Page<Event>> findAllByUserId(Long userId, Pageable pageRequest);
    Optional<Event> findByMessageContainingIgnoreCase(String message);
   }
