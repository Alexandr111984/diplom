package ru.netology.diplom.model;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_READ("ROLE_READ"),
    ROLE_WRITE("ROLE_WRITE");

    private final String nameRole;

    @Override
    public String getAuthority() {
        return nameRole;
    }
}
