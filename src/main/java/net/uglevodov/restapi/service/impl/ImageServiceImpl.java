package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.Image;
import net.uglevodov.restapi.entities.ImageLike;
import net.uglevodov.restapi.entities.User;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.repositories.ImageRepository;
import net.uglevodov.restapi.repositories.UserRepository;
import net.uglevodov.restapi.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Autowired
    ImageRepository repository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Image save(Image image) {
        log.trace("[{}] - Saving image {}", this.getClass().getSimpleName(), image);
        return repository.save(image);
    }

    @Override
    public Image get(long id) throws NotFoundException {
        log.trace("[{}] - Getting image id = ", this.getClass().getSimpleName(), id);

        return repository.findById(id).orElseThrow(()-> new NotFoundException("image id " + id + " not found"));
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
        Image image = repository.findById(id).orElseThrow(()-> new NotFoundException("image id " + id + " not found"));

        repository.delete(image);
    }

    @Override
    public Page<Image> getAll(Pageable pageRequest) {
        log.trace("[{}] - Getting images list", this.getClass().getSimpleName());

        return repository.findAll(pageRequest);
    }


    @Override
    public Image likeUnlike(Long userId, Long imageId) throws NotFoundException {
        ImageLike like = new ImageLike();
        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundException("user id " + userId + " not found"));
        like.setUser(user);
        Image image = repository.findById(imageId).orElseThrow(()-> new NotFoundException("image id " + imageId + " not found"));
        ImageLike alreadyLiked = image.getLikes().stream().filter(l -> l.getUser().getId()==userId).findFirst().orElse(null);

        if (alreadyLiked!=null) image.getLikes().remove(alreadyLiked);
        else
        image.getLikes().add(like);

        return repository.saveAndFlush(image);
    }


}
