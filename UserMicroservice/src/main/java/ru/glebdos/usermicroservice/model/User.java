package ru.glebdos.usermicroservice.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "customer", schema = "clients")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "first_name")
    @NotNull
    @Size(min = 2, max = 50)
    private String firstName;
    @Column(name = "second_name")
    @NotNull
    @Size(min = 2, max = 50)
    private String secondName;
    @Column(name = "email")
    @NotNull
    @Email
    @Size(max = 50)
    private  String email;
    @Column(name = "phone_number")
    @Pattern(regexp = "^\\+7[0-9]{10}$", message = "Неправильный формат телефонного номера. Ожидаемый формат: +79219008833")
    @NotNull
    private String phoneNumber;
    @Column(name = "address")
    @NotNull
    @Size(max = 100)
    private String address;
    @NotNull
    @Column(name = "registration_time",updatable = false)
    private LocalDateTime userRegistrationTime;


    @Column(name = "password")
    private String password;



    public User(String firstName, String secondName, String email, String phoneNumber, String address,String password ) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
    }
}
