/*
 * Copyright (c) 2020. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.Comment;
import net.uglevodov.restapi.entities.Hero;
import net.uglevodov.restapi.entities.Post;

public interface HeroesService extends GenericService<Hero> {

    Hero likeUnlike(Long userId, Long heroId);
    Hero addComment(Long userId, Comment comment, Long heroId);
    Hero deleteComment(Long userId, Comment comment, Long heroId);

}
