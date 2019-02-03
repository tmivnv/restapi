package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.Post;
import net.uglevodov.restapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostsRepository extends JpaRepository<Post, Long> {
}
