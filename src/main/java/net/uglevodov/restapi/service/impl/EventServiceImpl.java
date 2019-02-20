package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.Event;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.repositories.EventRepository;
import net.uglevodov.restapi.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public Event save(Event event) {
        log.trace("[{}] - Saving event {}", this.getClass().getSimpleName(), event);
        return eventRepository.save(event);
    }

    @Override
    public Event get(long id) throws NotFoundException {
        log.trace("[{}] - Getting event id = ", this.getClass().getSimpleName(), id);

        return eventRepository.findById(id).orElseThrow(() -> new NotFoundException("event id " + id + " not found"));
    }

    @Override
    public void update(Event entity) throws NotUpdatableException {

    }

    @Override
    public void delete(long id) throws NotFoundException {

    }

    @Override
    public Page<Event> getAll(Pageable pageRequest) {
        return null;
    }

    @Override
    public Page<Event> getAllByUserId(Long userId, Pageable pageRequest) {
        log.trace("[{}] - Getting events list for user "+userId, this.getClass().getSimpleName());

        return eventRepository.findAllByUserId(userId, pageRequest).orElseThrow(()-> new NotFoundException("user not found"));
    }
}
