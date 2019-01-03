package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.repositories.UserRepository;
import net.uglevodov.restapi.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "userDetailsService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository repository;

    @Autowired
    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String authData) throws NotFoundException {
        log.trace("[{}] - Loading user auth name = {}", this.getClass().getSimpleName(), authData);
        var user = repository.getByEmailOrNickname(authData).orElseThrow(() ->
                new NotFoundException("User authData = " + authData + " not found!"));

        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(Long id) {
        log.trace("[{}] - Loading user id = {}", this.getClass().getSimpleName(), id);
        var user = repository.findById(id).orElseThrow(() -> new NotFoundException("User id = " + id + " not found!"));

        return UserPrincipal.create(user);
    }
}