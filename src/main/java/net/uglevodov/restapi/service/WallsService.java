package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.Post;
import net.uglevodov.restapi.entities.Wall;

public interface WallsService extends GenericOwnedService<Wall> {
    Wall getByUser(Long userId);
    Wall addPost(Wall wall, Post post);
    Wall removePost(Wall wall, Post post);
    Wall updatePost(Wall wall, Post post);
}
