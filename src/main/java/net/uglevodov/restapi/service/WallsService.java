package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.Wall;

public interface WallsService extends GenericOwnedService<Wall> {
    Wall getByUser(Long userId);
}
