package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.User;
import net.uglevodov.restapi.exceptions.NotFoundException;

public interface UserService extends GenericService<User> {
    void setActive(long id, boolean active) throws NotFoundException;
    boolean checkEmailAvailable(String email);
    boolean checkNicknameAvailable(String nickname);
}
