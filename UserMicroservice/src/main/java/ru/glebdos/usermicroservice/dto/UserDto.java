package ru.glebdos.usermicroservice.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {


    @NotNull
    @Size(min = 2, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 50)
    private String secondName;

    @NotNull
    @Email
    @Size(max = 50)
    private String email;

    @Pattern(regexp = "^\\+7[0-9]{10}$", message = "Неправильный формат телефонного номера. Ожидаемый формат: +79219008833")
    @NotNull
    private String phoneNumber;

    @NotNull
    @Size(max = 100)
    private String address;


    @NotNull
    @Size(min = 6, message = "Пароль должен содержать не менее 6 символов")
    private String password; // Новое поле

}
