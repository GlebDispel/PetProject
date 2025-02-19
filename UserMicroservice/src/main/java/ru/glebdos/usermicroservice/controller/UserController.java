package ru.glebdos.usermicroservice.controller;



import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.glebdos.usermicroservice.dto.DynamicDto;
import ru.glebdos.usermicroservice.dto.UserDto;
import ru.glebdos.usermicroservice.service.UserService;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

   private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping(value = "/registration")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDto userDto) throws ExecutionException, InterruptedException {
        LOGGER.info("Creating user: {}", userDto);
        userService.createUser(userDto);
        return ResponseEntity.ok("Пользователь создан");
    }

    @GetMapping("/search")
    public ResponseEntity<DynamicDto> getUserByPhoneNumber(@RequestParam String phoneNumber) {

        return ResponseEntity.ok(userService.getUserByPhoneNumber(phoneNumber));

    }


    @PostMapping(value = "/update")
    public ResponseEntity<String> updateUser(@RequestBody @Valid DynamicDto updateUserDto,
                                                   @RequestParam String phoneNumber) {
        LOGGER.info("Number here : {}",phoneNumber);

        LOGGER.info("User here : {}", updateUserDto);
        userService.updateUser(updateUserDto, phoneNumber);

        return ResponseEntity.ok("Пользователь обновлен");

    }


    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String phoneNumber) {
        userService.deleteUser(phoneNumber);
        return ResponseEntity.ok("Пользователь удален");
    }


}
