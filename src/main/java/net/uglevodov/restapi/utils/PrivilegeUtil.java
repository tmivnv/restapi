package net.uglevodov.restapi.utils;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class PrivilegeUtil {
    private PrivilegeUtil() {}

    public static boolean hasRole(String role, Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().anyMatch((authority) -> (authority.getAuthority().toUpperCase().equals(role.toUpperCase())));
    }
}
