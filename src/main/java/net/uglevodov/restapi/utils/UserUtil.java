package net.uglevodov.restapi.utils;

import net.uglevodov.restapi.dto.ProfileDto;
import net.uglevodov.restapi.dto.SignupDto;
import net.uglevodov.restapi.dto.UserUpdateRequestDto;
import net.uglevodov.restapi.entities.User;
import net.uglevodov.restapi.entities.UserRole;
import net.uglevodov.restapi.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserUtil {

    @Autowired
    ImageService imageService;


    private UserUtil() {}

    public User prepareToSave(User user) {
        user.setPassword(PasswordUtil.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());

        return user;
    }

    public User updateFromUpdateRequest(UserUpdateRequestDto updateRequest, User user) {
        if (updateRequest.getAvatar()!=null) user.setAvatar(imageService.get(updateRequest.getAvatar()));
        if (updateRequest.getFirstName()!=null) user.setFirstName(updateRequest.getFirstName());
        if (updateRequest.getLastName()!=null) user.setLastName(updateRequest.getLastName());
        if (updateRequest.getIsWoman()!=null) user.setWoman(updateRequest.getIsWoman());
        if (updateRequest.getIsActive()!=null) user.setActive(updateRequest.getIsActive());
        if (updateRequest.getIsNew()!=null) user.setNewUser(updateRequest.getIsNew());

        return user;
    }

    public User signUpFromSignUpDto(SignupDto signupDto) {
        User user = new User();
                user.setEmail(signupDto.getEmail());
                user.setPassword(signupDto.getPassword());
                user.setNickname(signupDto.getNickname());
                user.setFirstName(signupDto.getFirstName());
                user.setLastName(signupDto.getLastName());
                user.setCreated(LocalDateTime.now());
                user.setAvatar(imageService.get(signupDto.getAvatar()));
                user.setActive(true);
                user.setRoles(Collections.singleton(UserRole.ROLE_USER));
                user.setWoman(true);
                user.setFollowingFavor(true);
                user.setFollowingLike(true);
                user.setFollowingNewFriend(true);
                user.setNewUser(true);

                return  user;

    }


    public static List<ProfileDto> usersToProfiles(List<User> users) {
        return users.stream()
                .map(ProfileDto::new)
                .collect(Collectors.toList());
    }
}
