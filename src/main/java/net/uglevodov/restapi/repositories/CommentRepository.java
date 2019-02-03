package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
