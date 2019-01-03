package net.uglevodov.restapi.utils;

import net.uglevodov.restapi.entities.BaseEntity;
import net.uglevodov.restapi.entities.Owned;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.WrongOwnerException;

public class ValidationUtil {
    private ValidationUtil() {}

    public static <T> T checkNotFound(T entity, String msg) throws NotFoundException {
        if (entity == null) {
            throw new NotFoundException(msg);
        }

        return entity;
    }

    public static void checkNotFoundWithId(boolean found, long id) throws NotFoundException {
        if (!found) {
            throw new NotFoundException("Not found entity id = " + id);
        }
    }

    public static <T extends Owned> T checkOwner(T entity, long userId) throws WrongOwnerException {
        if (entity.getUser().getId() != userId) {
            throw new WrongOwnerException("This entity does not belong to the user id = " + userId);
        }
        return entity;
    }

    public static <T extends BaseEntity> void checkNew (T entity) throws NotUpdatableException {
        if (entity.isNew()) {
            throw new NotUpdatableException("Failed to update. Post must not be new");
        }
    }
}