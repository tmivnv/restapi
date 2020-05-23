/*
 * Copyright (c) 2020. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.*;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.exceptions.WrongOwnerException;
import net.uglevodov.restapi.repositories.CommentRepository;
import net.uglevodov.restapi.repositories.HeroesRepository;
import net.uglevodov.restapi.repositories.PostsRepository;
import net.uglevodov.restapi.repositories.UserRepository;
import net.uglevodov.restapi.service.EventService;
import net.uglevodov.restapi.service.HeroesService;
import net.uglevodov.restapi.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class HeroesServiceImpl implements HeroesService {

    @Autowired
    private HeroesRepository heroesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EventService eventService;

    @Override
    public Hero save(Hero hero) {
         return heroesRepository.saveAndFlush(hero);
    }

    @Override
    public Hero get(long id) throws NotFoundException {
        log.trace("[{}] - Getting hero id = ", this.getClass().getSimpleName(), id);

        return heroesRepository.findById(id).orElseThrow(() -> new NotFoundException("post id " + id + " not found"));
    }

    @Override
    public void update(Hero hero) throws NotFoundException {
        Hero h = heroesRepository.findById(hero.getId()).orElseThrow(()-> new NotFoundException("hero not found"));

        heroesRepository.saveAndFlush(hero);

    }

    @Override
    public void delete(long id) throws NotFoundException {

        Hero hero = heroesRepository.findById(id).orElseThrow(() -> new NotFoundException("hero id " + id + " not found"));

        hero.setCommentSet(null);
        hero.setLikes(null);
        heroesRepository.saveAndFlush(hero);
        heroesRepository.delete(hero);
    }


    @Override
    public Page<Hero> getAll(Pageable pageRequest) {
        log.trace("[{}] - Getting posts list", this.getClass().getSimpleName());

        return heroesRepository.findAll(pageRequest);
    }

    @Override
    public Hero likeUnlike(Long userId, Long heroId) throws NotFoundException {
        HeroLike like = new HeroLike();
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user id " + userId + " not found"));
        like.setUser(user);
        like.setCreated(LocalDateTime.now());
        like.setUserId(userId);

        Hero hero = heroesRepository.findById(heroId).orElseThrow(() -> new NotFoundException("post id " + heroId + " not found"));
        HeroLike alreadyLiked = hero.getLikes().stream().filter(l -> l.getUser().getId().equals(userId)).findFirst().orElse(null);

        if (alreadyLiked != null) hero.getLikes().remove(alreadyLiked);
        else {
            hero.getLikes().add(like);

        }


        return heroesRepository.saveAndFlush(hero);
    }

    @Override
    public Hero addComment(Long userId, Comment comment, Long heroId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user id " + userId + " not found"));
        Hero hero = heroesRepository.findById(heroId).orElseThrow(() -> new NotFoundException("post id " + heroId + " not found"));
        comment.setUser(user);
        comment.setUserId(userId);

        hero.getCommentSet().add(comment);


        return heroesRepository.saveAndFlush(hero);
    }

    @Override
    public Hero deleteComment(Long userId, Comment comment, Long heroId) {
        if (comment == null) throw new NotFoundException("comment not found");
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user id " + userId + " not found"));
        Hero hero = heroesRepository.findById(heroId).orElseThrow(() -> new NotFoundException("post id " + heroId + " not found"));

        if (!comment.getUser().equals(user)&&!user.getRoles().contains(UserRole.ROLE_ADMIN)) //если не автор коммента или не админ - ошибка
            throw new WrongOwnerException("This comment can not be deleted by this user");

        hero.getCommentSet().remove(comment);
        comment.setImageSet(null);
        commentRepository.delete(comment);

        return heroesRepository.saveAndFlush(hero);

    }

   }
