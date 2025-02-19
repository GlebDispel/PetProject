package ru.glebdos.usermicroservice.unittest.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.glebdos.usermicroservice.dto.DynamicDto;
import ru.glebdos.usermicroservice.dto.UserDto;
import ru.glebdos.usermicroservice.model.User;
import ru.glebdos.usermicroservice.repository.UserRepository;
import ru.glebdos.usermicroservice.service.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    private UserDto userDto;
    private User user;


    @BeforeEach
    void setUp() {
        userDto = new UserDto(
                "Fred", "Pink",
                "fred@mail.ru", "+79998887766", "street 3", "1234567");
        user = new User("Fred", "Pink",
                "fred@mail.ru", "+79998887766", "street 3","1234567");
    }

    @SneakyThrows
    @Test
    @DisplayName("Успешное создание пользователя при коректных данных")
    void createUserTest_shouldCreateUser_whenUserValid() {

        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        userService.createUser(userDto);

        verify(userRepository, times(1)).save(user);
        assertNotNull(user.getUserRegistrationTime());


    }

    @Test
    @DisplayName("Получение пользователя,если он существует")
    void getUserTest_shouldReturnUser_whenUserExists() {
        String phoneNumber = "+79998887766";

        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        DynamicDto returnedUserDto = userService.getUserByPhoneNumber(phoneNumber);

        verify(userRepository, times(1)).findByPhoneNumber(phoneNumber);
        assertEquals(user.getFirstName(), returnedUserDto.getFirstName());
        assertEquals(user.getSecondName(), returnedUserDto.getSecondName());
        assertEquals(user.getPhoneNumber(), returnedUserDto.getPhoneNumber());
        assertEquals(user.getEmail(), returnedUserDto.getEmail());
        assertEquals(user.getAddress(), returnedUserDto.getAddress());


    }

    @Test
    @DisplayName("Ошибка,если пользователь не существует")
    void getUserTest_shouldThrowEntityNotFoundException_whenUserDoesNotExist() {
        String phoneNumber = "+79998887766";

        when(userRepository.findByPhoneNumber(phoneNumber)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> userService.getUserByPhoneNumber(phoneNumber));
    }

    @Test
    @DisplayName("Ошибка,если неверный формат телефона")
    void getUserTest_shouldThrowIllegalArgumentException_whenInvalidPhoneNumber() {
        String invalidPhoneNumber = "+7986";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService
                .getUserByPhoneNumber(invalidPhoneNumber));

        assertEquals("Неправильный формат телефонного номера. Ожидаемый формат: +79219008833", exception.getMessage());

    }

    @Test
    @DisplayName("Частичное обновление,если пользователь существует")
    void updateUserTest_shouldUpdatePartOfUser_whenUserExists() {
        DynamicDto newDynamicDto = new DynamicDto();
        newDynamicDto.setFirstName("Fred");

        String phoneNumber = "+79998887766";

        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));


        userService.updateUser(newDynamicDto, phoneNumber);

        verify(userRepository, times(1)).save(user);
        assertEquals(newDynamicDto.getFirstName(), user.getFirstName());


    }

    @Test
    @DisplayName("Полное обновление,если пользователь существует")
    void updateUserTest_shouldUpdateFullUser_whenUserExists() {
        DynamicDto newDynamicDto = new DynamicDto("Fred", "Yellow",
                "frenk@gmail.com", "+72287771488", "street 4");
        String phoneNumber = "+79998887766";


        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));


        userService.updateUser(newDynamicDto, phoneNumber);

        verify(userRepository, times(1)).save(user);
        assertEquals(newDynamicDto.getFirstName(), user.getFirstName());
        assertEquals(newDynamicDto.getSecondName(), user.getSecondName());
        assertEquals(newDynamicDto.getPhoneNumber(), user.getPhoneNumber());
        assertEquals(newDynamicDto.getEmail(), user.getEmail());
        assertEquals(newDynamicDto.getAddress(), user.getAddress());


    }

    @Test
    @DisplayName("Удаление,если пользователь существует")
    void deleteUserTest_shouldDeleteUser_whenUserExists() {
        String phoneNumber = "+79998887766";


        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));

        userService.deleteUser(phoneNumber);

        verify(userRepository, times(1)).deleteUserByPhoneNumber(phoneNumber);

    }


}
