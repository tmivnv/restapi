package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RestResource(exported = false)
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :accessname OR u.nickname = :accessname")
    Optional<User> getByEmailOrNickname(@Param("accessname") String email);

    Optional<User> findById(Long id);

    @Override
    User getOne(Long id);

    @Transactional
    @Override
    User save(User user);

    @Override
    List<User> findAll(Sort sort);



    int countAllByNickname(String nickname);

    int countAllByEmail(String email);
}
