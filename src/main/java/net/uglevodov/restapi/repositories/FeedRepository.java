package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.FeedEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<FeedEntry, Long> {
    Optional<List<FeedEntry>> findAllByUserId(Long userId);
}
