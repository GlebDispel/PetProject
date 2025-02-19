package ru.glebdos.usermicroservice.service;


import ru.glebdos.usermicroservice.dto.DynamicDto;
import ru.glebdos.usermicroservice.dto.UserDto;

import java.util.concurrent.ExecutionException;

public interface UserService {


     void createUser(UserDto userDto) throws ExecutionException, InterruptedException;
     DynamicDto getUserByPhoneNumber(String phoneNumber);

     void updateUser(DynamicDto updateUserDto, String phoneNumber);

     void deleteUser(String userPhoneNumber);
}
