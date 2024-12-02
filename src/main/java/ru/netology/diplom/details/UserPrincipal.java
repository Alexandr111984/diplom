package ru.netology.diplom.details;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.netology.diplom.entity.UserEntity;

import java.util.Collection;

public class UserPrincipal implements UserDetails {

    private final UserEntity user;

    public UserPrincipal(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    public Integer getId() {
        return user.getId();
    }

}
