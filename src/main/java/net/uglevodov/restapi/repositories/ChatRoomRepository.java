package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.ChatRoomEntry;
import net.uglevodov.restapi.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntry, Long> {
    Optional<ChatRoomEntry> findByPost(Post post);
}
