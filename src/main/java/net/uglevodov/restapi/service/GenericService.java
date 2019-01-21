package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.BaseEntity;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GenericService<T extends BaseEntity> {
    T save(T entity);
    T get(long id) throws NotFoundException;
    void update(T entity) throws NotUpdatableException;
    void delete(long id) throws NotFoundException;
    Page<T> getAll(Pageable pageRequest);
}
