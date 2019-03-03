/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.*;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.repositories.DishesRepository;
import net.uglevodov.restapi.repositories.ImageRepository;
import net.uglevodov.restapi.repositories.UserRepository;
import net.uglevodov.restapi.service.EventService;
import net.uglevodov.restapi.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventService eventService;


    @Override
    public Image save(Image image) {
        log.trace("[{}] - Saving image {}", this.getClass().getSimpleName(), image);
        return repository.save(image);
    }

    @Override
    public Image get(long id) throws NotFoundException {
        log.trace("[{}] - Getting image id = ", this.getClass().getSimpleName(), id);

        return repository.findById(id).orElseThrow(() -> new NotFoundException("image id " + id + " not found"));
    }

    @Override
    public void update(Image image) throws NotUpdatableException {
        log.trace("[{}] - Updating image {}", this.getClass().getSimpleName(), image);

        Assert.notNull(image, "Image can not be null");
        repository.save(image);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        log.trace("[{}] - Deleting image id = {}", this.getClass().getSimpleName(), id);
        Image image = repository.findById(id).orElseThrow(() -> new NotFoundException("image id " + id + " not found"));

        try {
            repository.delete(image);
        } catch (Exception e) {
            throw new NotUpdatableException("This ingredient is used somewhere and can not be deleted ");
        }
    }

    @Override
    public Page<Image> getAll(Pageable pageRequest) {
        log.trace("[{}] - Getting images list", this.getClass().getSimpleName());

        return repository.findAll(pageRequest);
    }


    @Override
    public Image likeUnlike(Long userId, Long imageId) throws NotFoundException {
        ImageLike like = new ImageLike();
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user id " + userId + " not found"));
        like.setUser(user);
        like.setCreated(LocalDateTime.now());
        like.setUserId(userId);
        Image image = repository.findById(imageId).orElseThrow(() -> new NotFoundException("image id " + imageId + " not found"));
        ImageLike alreadyLiked = image.getLikes().stream().filter(l -> l.getUser().getId().equals(userId)).findFirst().orElse(null);

        if (alreadyLiked != null) image.getLikes().remove(alreadyLiked);
        else {
            image.getLikes().add(like);
            //рассказываем фолловерам об этом событии
            for (Follower follower : user.getFollowers()) {

                User followerUser = userRepository.findById(follower.getFollowerId()).orElseThrow(() -> new NotFoundException("user id " + follower.getFollowerId() + " not found"));
                if (followerUser.isFollowingFavor()) { //если фолловер хочет видеть этот тип события
                    Event event = new Event();
                    event.setRead(false);
                    event.setCreated(LocalDateTime.now());
                    event.setLink("/api/files/downloadFileId/" + imageId);
                    event.setType("photo_like");
                    event.setUser(followerUser);
                    event.setMessage(user.getFirstName() + " " + user.getLastName() + (user.isWoman() ? " оценила" : " оценил") + " фотографию");
                    if (eventService.findByMessageContainingIgnoreCase(event.getMessage()) == null) //если еще нет такого события
                        eventService.save(event);
                }
            }
        }

        return repository.saveAndFlush(image);
    }


}
