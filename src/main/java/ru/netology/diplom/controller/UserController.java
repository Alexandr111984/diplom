package ru.netology.diplom.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.diplom.dto.UserDTO;
import ru.netology.diplom.model.Token;
import ru.netology.diplom.repository.UsersRepository;
import ru.netology.diplom.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class UserController {
    private final UsersRepository usersRepository;

    private final UserService userService;


    @PostMapping("login")
    public ResponseEntity<Token> login(@RequestBody UserDTO userDTO) {
        log.info("Попытка авторизоваться на сервере");
        Token token = userService.login(userDTO);
        return ResponseEntity.ok(token);
    }

}
