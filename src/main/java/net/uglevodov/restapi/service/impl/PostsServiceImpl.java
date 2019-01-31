package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.Post;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.exceptions.WrongOwnerException;
import net.uglevodov.restapi.repositories.PostsRepository;
import net.uglevodov.restapi.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PostsServiceImpl implements PostsService {

    @Autowired
    PostsRepository postsRepository;

    @Override
    public Post save(Post owned, long userId) throws WrongOwnerException {
        return null;
    }

    @Override
    public Post get(long id) throws NotFoundException {
        log.trace("[{}] - Getting post id = ", this.getClass().getSimpleName(), id);

        return postsRepository.findById(id).orElseThrow(() -> new NotFoundException("post id " + id + " not found"));
    }

    @Override
    public void update(Post owned, long userId) throws NotUpdatableException, WrongOwnerException {

    }

    @Override
    public void delete(long id, long userId) throws NotFoundException {

    }

    @Override
    public void delete(long id) throws NotFoundException {

    }

    @Override
    public List<Post> getAll(long userId) throws NotFoundException {
        return null;
    }
}
