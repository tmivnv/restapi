package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EventService extends GenericService<Event> {
    Page<Event> getAllByUserId(Long userId, Pageable pageRequest);
    Event findByMessageContainingIgnoreCase(String message);
}
