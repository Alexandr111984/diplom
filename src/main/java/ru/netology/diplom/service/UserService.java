package ru.netology.diplom.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.netology.diplom.details.UserPrincipal;
import ru.netology.diplom.repository.UsersRepository;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UsersRepository usersRepository;


    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = usersRepository.findByLogin(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return new UserPrincipal(user.get());
    }

}
