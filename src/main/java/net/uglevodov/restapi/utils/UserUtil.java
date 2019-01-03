package net.uglevodov.restapi.utils;

import net.uglevodov.restapi.dto.ProfileDto;
import net.uglevodov.restapi.dto.UserUpdateRequestDto;
import net.uglevodov.restapi.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserUtil {
    private UserUtil() {}

    public static User prepareToSave(User user) {
        user.setPassword(PasswordUtil.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());

        return user;
    }

    public static User updateFromUpdateRequest(UserUpdateRequestDto updateRequest, User user) {
        user.setAvatar(updateRequest.getAvatar());
        user.setFirstName(updateRequest.getFirstName());
        user.setLastName(updateRequest.getLastName());

        return user;
    }

    public static List<ProfileDto> usersToProfiles(List<User> users) {
        return users.stream()
                .map(ProfileDto::new)
                .collect(Collectors.toList());
    }
}
