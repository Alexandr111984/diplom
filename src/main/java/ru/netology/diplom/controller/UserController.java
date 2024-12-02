package ru.netology.diplom.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.diplom.dto.UserDTO;
import ru.netology.diplom.model.Token;
import ru.netology.diplom.service.JWTTokenService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final JWTTokenService jwtTokenService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Token> login(@RequestBody UserDTO userDTO) {
        log.info("Попытка авторизоваться на сервере");
        Token token = jwtTokenService.login(userDTO);
        return ResponseEntity.ok(token);
    }

}
