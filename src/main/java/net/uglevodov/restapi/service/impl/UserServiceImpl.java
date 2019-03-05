/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.User;
import net.uglevodov.restapi.entities.UserInfo;
import net.uglevodov.restapi.exceptions.NotAvailableException;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.repositories.UserRepository;
import net.uglevodov.restapi.service.UserService;
import net.uglevodov.restapi.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


import java.util.List;
import java.util.Set;

import static net.uglevodov.restapi.utils.ValidationUtil.checkNotFound;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private UserRepository repository;

    @Autowired
    UserUtil utils;


    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public User save(User user) {
        log.trace("[{}] - Saving user {}", this.getClass().getSimpleName(), user);
        return repository.save(utils.prepareToSave(user));
    }

    @Override
    public User get(long id) throws NotFoundException {
        log.trace("[{}] - Getting user id = ", this.getClass().getSimpleName(), id);

        return repository.findById(id).orElseThrow(()-> new NotFoundException("user not found"));
    }

    @Transactional
    @Override
    public void update(User user) throws NotUpdatableException {
        log.trace("[{}] - Updating user {}", this.getClass().getSimpleName(), user);
        if (user.isNew()) {
            throw new NotUpdatableException("Failed to update. Reason: user is new");
        }

        Assert.notNull(user, "User can not be null");
        repository.save(utils.prepareToSave(user));
    }

    @Transactional
    @Override
    public void delete(long id) throws NotFoundException {
        log.trace("[{}] - Deleting user id = {}", this.getClass().getSimpleName(), id);

        User user = repository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        repository.delete(user);
    }

    @Override
    public Page<User> getAll(Pageable pageRequest) {
        log.trace("[{}] - Getting users list", this.getClass().getSimpleName());

        return repository.findAll(pageRequest);
    }

    @Override
    public boolean checkEmailAvailable(String email) {
        log.trace("[{}] - Checking email availability", this.getClass().getSimpleName());

        if (repository.countAllByEmail(email) != 0) throw new NotAvailableException("Email already exists");

        return repository.countAllByEmail(email) == 0;
    }

    @Override
    public boolean checkNicknameAvailable(String nickname) {
        log.trace("[{}] - Checking nickname availability", this.getClass().getSimpleName());

        if (repository.countAllByNickname(nickname) != 0) throw new NotAvailableException("Nickname already exists");


        return repository.countAllByNickname(nickname) == 0;
    }

    @Transactional
    @Override
    public void setActive(long id, boolean active) throws NotFoundException {
        log.trace("[{}] - Setting user's (id = {}) active status to {}", this.getClass().getSimpleName(), id, active);
        var user = repository.findById(id).orElseThrow(()-> new NotFoundException("user not found"));
        user.setActive(active);
        repository.save(user);
    }

    @Override
    public User addUserInfo(User user, UserInfo userInfo) {
        userInfo.setUser(user);
        userInfo.setUserId(user.getId());
        user.getUserInfo().add(userInfo);
        return repository.saveAndFlush(user);
    }

    @Override
    public User updateUserInfo(User user, UserInfo userInfo) {

        user.getUserInfo().stream().filter(u->u.getProp().equals(userInfo.getProp())).findFirst().orElseThrow(()-> new NotFoundException("property not found")).setPropValue(userInfo.getPropValue());

        return repository.saveAndFlush(user);
    }

    @Override
    public void removeUserInfo(User user, Long userInfoId) {

       UserInfo toRemove =
                user.getUserInfo().stream().filter(u->u.getId().equals(userInfoId)).findFirst().orElseThrow(()-> new NotFoundException("property not found"));

       repository.deleteUserInfo(toRemove.getId());

    }

    @Override
    public List<Long> allUserIds() {
        return repository.getUserIds();
    }
}