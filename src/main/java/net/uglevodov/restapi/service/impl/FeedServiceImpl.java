package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.FeedEntry;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.repositories.FeedRepository;
import net.uglevodov.restapi.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FeedServiceImpl implements FeedService {

    @Autowired
    FeedRepository feedRepository;

    @Override
    public FeedEntry save(FeedEntry feedEntry) {
        log.trace("[{}] - Saving feed entry {}", this.getClass().getSimpleName(), feedEntry);
        return feedRepository.save(feedEntry);
    }

    @Override
    public FeedEntry get(long id) throws NotFoundException {
        log.trace("[{}] - Getting feed id = ", this.getClass().getSimpleName(), id);

        return feedRepository.findById(id).orElseThrow(() -> new NotFoundException("feed id " + id + " not found"));
    }

    @Override
    public Page<FeedEntry> findAllByUserId(Long userId, Pageable pageRequest) {
        log.trace("[{}] - Getting feed for user id = ", this.getClass().getSimpleName(), userId);
        List<FeedEntry> feed = feedRepository.findAllByUserId(userId).orElseThrow(() -> new NotFoundException("user id " + userId + " not found"));

        return new PageImpl<>(new ArrayList<>(feed), pageRequest, feed.size());
    }

    @Override
    public void update(FeedEntry feedEntry) throws NotUpdatableException {
        log.trace("[{}] - Updating feed entry {}", this.getClass().getSimpleName(), feedEntry);
        feedRepository.findById(feedEntry.getId()).orElseThrow(() -> new NotFoundException("feed entry id " + feedEntry.getId() + " not found"));
        Assert.notNull(feedEntry, "feed entry can not be null");
        feedRepository.save(feedEntry);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        log.trace("[{}] - Deleting feed entry id = {}", this.getClass().getSimpleName(), id);
        FeedEntry feedEntry = feedRepository.findById(id).orElseThrow(() -> new NotFoundException("feed entry id " + id + " not found"));

        feedRepository.delete(feedEntry);
    }

    @Override
    public Page<FeedEntry> getAll(Pageable pageRequest) {
        log.trace("[{}] - Getting all feeds", this.getClass().getSimpleName());

        return feedRepository.findAll(pageRequest);
    }
}
