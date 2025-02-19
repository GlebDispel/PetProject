package ru.glebdos.usermicroservice.unittest.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import ru.glebdos.usermicroservice.controller.UserController;
import ru.glebdos.usermicroservice.dto.DynamicDto;
import ru.glebdos.usermicroservice.dto.UserDto;
import ru.glebdos.usermicroservice.service.UserService;
import ru.glebdos.usermicroservice.util.GlobalExceptionHandler;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {


    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private ObjectMapper objectMapper;
    private UserDto validUserDto;
    private DynamicDto validDynamicDto;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        validUserDto = new UserDto(
                "John", "Doe", "john.doe@example.com",
                "+79219008833", "123 Main St","123456");
        validDynamicDto = new DynamicDto("John", "Doe", "john.doe@example.com",
                "+79219008833", "123 Main St");
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    @SneakyThrows
    void createUser_ShouldReturnOk_WhenValidUserDto() {
        String jsonContent = objectMapper.writeValueAsString(validUserDto);

        mockMvc.perform(post("/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .accept(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))

                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь создан"));


        verify(userService, times(1)).createUser(validUserDto);

    }

    @Test
    @DisplayName("Ошибка, пользователь с таким мылом уже существует")
    @SneakyThrows
    void createUser_shouldReturnConflict_whenEmailAlreadyExists() {
        String jsonContent = objectMapper.writeValueAsString(validUserDto);
        String exceptionMessage = "Пользователь с таким электронным адресом уже существует";

        doThrow(new DataIntegrityViolationException(exceptionMessage)).when(userService).createUser(validUserDto);
        mockMvc.perform(post("/users/registration")
        .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(exceptionMessage));
    }
    @Test
    @DisplayName("Ошибка, пользователь с таким номером телефона уже существует")
    @SneakyThrows
    void createUser_shouldReturnConflict_whenPhoneNumberAlreadyExists() {
        String jsonContent = objectMapper.writeValueAsString(validUserDto);
        String exceptionMessage = "Пользователь с таким номером телефона уже существует";

        doThrow(new DataIntegrityViolationException(exceptionMessage)).when(userService).createUser(validUserDto);
        mockMvc.perform(post("/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(exceptionMessage));
    }

    @Test
    @DisplayName("Ошибка создания, не задано имя пользователя")
    @SneakyThrows
    void createUser_shouldReturnBadRequest_WhenFirstNameIsNull() {

        UserDto invalidUserDto = validUserDto;
        invalidUserDto.setFirstName(null);

        String jsonContent = objectMapper.writeValueAsString(invalidUserDto);

        mockMvc.perform(post("/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("firstName - must not be null"))
                .andReturn();

        verify(userService, never()).createUser(validUserDto);

    }

    @Test
    @DisplayName("Ошибка создания, неверный формат номера телефона")
    @SneakyThrows
    void createUser_shouldReturnBadRequest_whenInvalidPhoneNumber() {
        UserDto invalidUserDto = validUserDto;
        invalidUserDto.setPhoneNumber("+09281234");
        String jsonContent = objectMapper.writeValueAsString(invalidUserDto);

        mockMvc.perform(post("/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("phoneNumber - Неправильный формат телефонного номера. " +
                                "Ожидаемый формат: +79219008833"))
                .andReturn();

        verify(userService, never()).createUser(validUserDto);


    }

    @Test
    @DisplayName("Ошибка создания, неверный формат почты")
    @SneakyThrows
    void createUser_shouldReturnBadRequest_whenInvalidEmail() {
        UserDto invalidUserDto = validUserDto;
        invalidUserDto.setEmail("some email");
        String jsonContent = objectMapper.writeValueAsString(invalidUserDto);

        mockMvc.perform(post("/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("email - must be a well-formed email address"))
                .andReturn();

        verify(userService, never()).createUser(validUserDto);

    }

    @Test
    @DisplayName("Успешное получение пользователя")
    @SneakyThrows
    void getUserByPhoneNumber_ShouldReturnOk_WhenValidPhoneNumber() {
        String phoneNumber = validDynamicDto.getPhoneNumber();

        when(userService.getUserByPhoneNumber(phoneNumber)).thenReturn(validDynamicDto);

        mockMvc.perform(get("/users/search")
                        .param("phoneNumber", phoneNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(validDynamicDto.getFirstName()))
                .andExpect(jsonPath("$.secondName").value(validDynamicDto.getSecondName()))
                .andExpect(jsonPath("$.phoneNumber").value(validDynamicDto.getPhoneNumber()))
                .andExpect(jsonPath("$.email").value(validDynamicDto.getEmail()))
                .andExpect(jsonPath("$.address").value(validDynamicDto.getAddress()));

        verify(userService, times(1)).getUserByPhoneNumber(phoneNumber);
    }

    @Test
    @DisplayName("Ошибка получения, неверный формат номера телефона")
    @SneakyThrows
    void getUserByPhoneNumber_shouldReturnBadRequest_whenInvalidPhoneNumber() {
        String invalidPhoneNumber = "12345";
        String illegalArgumentExceptionMessage = "Неправильный формат телефонного номера. Ожидаемый формат: +79219008833";

        doThrow(new IllegalArgumentException(illegalArgumentExceptionMessage))
                .when(userService).getUserByPhoneNumber(anyString());

        mockMvc.perform(get("/users/search")
                        .param("phoneNumber", invalidPhoneNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(illegalArgumentExceptionMessage));

    }

    @Test
    @DisplayName("Ошибка получения, отсуствует обязательный параметр")
    @SneakyThrows
    void getUserByPhoneNumber_shouldReturnBadRequest_whenNullParameter() {


        mockMvc.perform(get("/users/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Required request parameter 'phoneNumber' " +
                                "for method parameter type String is not present"))
                .andReturn();


    }

    @Test
    @DisplayName("Ошибка получения, пользователь отсутствует")
    @SneakyThrows
    void getUserByPhoneNumber_shouldReturnEntityNotFound_whenUserNotFound() {
        String phoneNumber = "+7770001122";
        doThrow(new EntityNotFoundException(phoneNumber))
                .when(userService).getUserByPhoneNumber(phoneNumber);

        mockMvc.perform(get("/users/search")
                        .param("phoneNumber", phoneNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Пользователь c номером " + phoneNumber + " не найден"));


    }

    @Test
    @DisplayName("Успешное полное обновление пользователя")
    @SneakyThrows
    void updateUser_shouldReturnOk_WhenFullUserUpdated() {
        String phoneNumber = validUserDto.getPhoneNumber();
        DynamicDto updatedUserDto = new DynamicDto("Gleb", "Chalov", "gleb@gmail.com",
                "+77777777777", "anything 17");
        String jsonContent = objectMapper.writeValueAsString(updatedUserDto);


        mockMvc.perform(post("/users/update")
                        .param("phoneNumber", phoneNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .accept(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь обновлен"));
    }

    @Test
    @DisplayName("Успешное частичное обновление пользователя")
    @SneakyThrows
    void updateUser_shouldReturnOk_whenPartialUserUpdated() {
        String phoneNumber = validUserDto.getPhoneNumber();
        DynamicDto updatedUserDto = new DynamicDto();
        updatedUserDto.setFirstName("Gleb");
        String jsonContent = objectMapper.writeValueAsString(updatedUserDto);


        mockMvc.perform(post("/users/update")
                        .param("phoneNumber", phoneNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .accept(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь обновлен"));
    }

    @Test
    @DisplayName("Ошибка обновления, пользователь не найден")
    @SneakyThrows
    void updateUser_shouldReturnEntityNotFound_whenUserNotFound() {
        String phoneNumber = "+7770001122";

        DynamicDto updatedUserDto = new DynamicDto();
        updatedUserDto.setFirstName("Gleb");

        String jsonContent = objectMapper.writeValueAsString(updatedUserDto);

        doThrow(new EntityNotFoundException(phoneNumber))
                .when(userService).updateUser(updatedUserDto, phoneNumber);

        mockMvc.perform(post("/users/update")
                        .param("phoneNumber", phoneNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Пользователь c номером " + phoneNumber + " не найден"));

    }

    @Test
    @DisplayName("Ошибка обновления, неверный формат номера телефона")
    @SneakyThrows
    void updateUser_ShouldReturnBadRequest_whenInvalidPhoneNumber() {
        String phoneNumber = "12345";
        String illegalArgumentExceptionMessage = "Неправильный формат телефонного номера. Ожидаемый формат: +79219008833";

        DynamicDto updatedUserDto = new DynamicDto();
        updatedUserDto.setFirstName("Gleb");

        String jsonContent = objectMapper.writeValueAsString(updatedUserDto);

        doThrow(new IllegalArgumentException(illegalArgumentExceptionMessage))
                .when(userService).updateUser(updatedUserDto, phoneNumber);

        mockMvc.perform(post("/users/update")
                        .param("phoneNumber", phoneNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(illegalArgumentExceptionMessage));


    }

    @Test
    @DisplayName("Ошибка обновления, отсуствует обязательный параметр")
    @SneakyThrows
    void updateUser_shouldReturnBadRequest_whenNullParameter() {

        DynamicDto updatedUserDto = new DynamicDto();
        updatedUserDto.setFirstName("Gleb");
        String jsonContent = objectMapper.writeValueAsString(updatedUserDto);

        mockMvc.perform(post("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Required request parameter 'phoneNumber' " +
                                "for method parameter type String is not present"))
                .andReturn();

        verify(userService, never()).updateUser(updatedUserDto, null);
    }

    @Test
    @DisplayName("Ошибка обновления, неверный формат почты")
    @SneakyThrows
    void updateUser_shouldReturnBadRequest_whenInvalidEmail() {
        String phoneNumber = "+7770001122";
        DynamicDto updatedUserDto = new DynamicDto();
        updatedUserDto.setEmail("jiga-driga");
        String jsonContent = objectMapper.writeValueAsString(updatedUserDto);

        mockMvc.perform(post("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("phoneNumber", phoneNumber)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("email - must be a well-formed email address"));
    }

    @Test
    @DisplayName("Ошибка обновления, пустое тело запроса")
    @SneakyThrows
    void updateUser_shouldReturnBadRequest_whenEmptyBodyRequest() {
        String phoneNumber = "+7770001122";
        DynamicDto updatedUserDto = new DynamicDto();
        String jsonContent = objectMapper.writeValueAsString(updatedUserDto);

        mockMvc.perform(post("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("phoneNumber", phoneNumber)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("valid - Для обновления должно быть указано хотя бы одно поле"));

    }

    @Test
    @DisplayName("Успешное удаление пользователя")
    @SneakyThrows
    void deleteUser_shouldReturnOk_whenUserDeleted() {
        String phoneNumber = "+7770001122";

        mockMvc.perform(delete("/users/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("phoneNumber", phoneNumber)
                        .accept(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь удален"));

        verify(userService, times(1)).deleteUser(phoneNumber);
    }

    @Test
    @DisplayName("Ошибка удаления, пользователь не найден")
    @SneakyThrows
    void deleteUser_shouldReturnEntityNotFound_whenUserNotFound() {
        String phoneNumber = "+7770001122";

        doThrow(new EntityNotFoundException(phoneNumber)).when(userService).deleteUser(phoneNumber);

        mockMvc.perform(delete("/users/delete")
                        .param("phoneNumber", phoneNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Пользователь c номером " + phoneNumber + " не найден"));

    }

    @Test
    @DisplayName("Ошибка удаления, неверный формат номера телефона")
    @SneakyThrows
    void deleteUser_shouldReturnBadRequest_whenInvalidPhoneNumber() {
        String invalidPhoneNumber = "12345";
        String illegalArgumentExceptionMessage = "Неправильный формат телефонного номера. Ожидаемый формат: +79219008833";


        doThrow(new IllegalArgumentException(illegalArgumentExceptionMessage))
                .when(userService).deleteUser(invalidPhoneNumber);

        mockMvc.perform(delete("/users/delete")
                        .param("phoneNumber", invalidPhoneNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(illegalArgumentExceptionMessage));
    }


    @Test
    @DisplayName("Ошибка удаления, отсутствует обязательный параметр")
    @SneakyThrows
    void deleteUser_shouldReturnBadRequest_whenNullParameter() {

        mockMvc.perform(delete("/users/delete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Required request parameter 'phoneNumber' " +
                        "for method parameter type String is not present"));
    }




}
