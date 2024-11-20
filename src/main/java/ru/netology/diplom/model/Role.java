package ru.netology.diplom.model;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    ROLE_ADMIN("ROLE_ADMIN");

    private final String nameRole;

    @Override
    public String getAuthority() {
        return nameRole;
    }
}
