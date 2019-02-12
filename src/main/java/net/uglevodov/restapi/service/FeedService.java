package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.FeedEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface FeedService extends GenericService<FeedEntry> {

    Page<FeedEntry> findAllByUserId(Long userId, Pageable pageRequest);
}
