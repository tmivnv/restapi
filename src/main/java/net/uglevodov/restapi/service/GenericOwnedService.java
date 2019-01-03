package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.Owned;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.exceptions.WrongOwnerException;

import java.util.List;

public interface GenericOwnedService<T extends Owned> {
    T save(T owned, long userId) throws WrongOwnerException;
    T get(long id) throws NotFoundException;
    void update(T owned, long userId) throws NotUpdatableException, WrongOwnerException;
    void delete(long id, long userId) throws NotFoundException;
    void delete(long id) throws NotFoundException;
    List<T> getAll(long userId) throws NotFoundException;
}