package ru.glebdos.usermicroservice.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicDto {

    @Size(min = 2, max = 50)
    private String firstName;


    @Size(min = 2, max = 50)
    private String secondName;


    @Email
    @Size(max = 50)
    private String email;

    @Pattern(regexp = "^\\+7[0-9]{10}$", message = "Неправильный формат телефонного номера. Ожидаемый формат: +79219008833")
    private String phoneNumber;

    @Size(max = 100)
    private String address;


    @AssertTrue(message = "Для обновления должно быть указано хотя бы одно поле")
    public boolean isValid() {
        return firstName != null || secondName != null || email != null ||
                phoneNumber != null || address != null;
    }
}
