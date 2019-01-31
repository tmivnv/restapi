package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Post, Long> {
}
