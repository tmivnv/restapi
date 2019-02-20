package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.User;
import net.uglevodov.restapi.entities.UserInfo;
import net.uglevodov.restapi.exceptions.NotFoundException;

import java.util.List;

public interface UserService extends GenericService<User> {
    void setActive(long id, boolean active) throws NotFoundException;
    boolean checkEmailAvailable(String email);
    boolean checkNicknameAvailable(String nickname);
    User addUserInfo(User user, UserInfo userInfo);
    User updateUserInfo(User user, UserInfo userInfo);
    void removeUserInfo(User user, Long userInfoId);
    List<Long> allUserIds();
}
