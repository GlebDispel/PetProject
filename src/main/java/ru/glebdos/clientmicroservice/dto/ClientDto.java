package ru.glebdos.clientmicroservice.dto;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public class ClientDto {


    @NotNull
    @Size(min = 2, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 50)
    private String secondName;

    @NotNull
    @Size(max = 50)
    private String email;

    @NotNull
    private String phoneNumber;

    @NotNull
    @Size(max = 100)
    private String address;

    public ClientDto(String firstName, String secondName, String email, String phoneNumber, String address) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public @NotNull @Size(min = 2, max = 50) String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotNull @Size(min = 2, max = 50) String firstName) {
        this.firstName = firstName;
    }

    public @NotNull @Size(min = 2, max = 50) String getSecondName() {
        return secondName;
    }

    public void setSecondName(@NotNull @Size(min = 2, max = 50) String secondName) {
        this.secondName = secondName;
    }

    public @NotNull @Size(max = 50) String getEmail() {
        return email;
    }

    public void setEmail(@NotNull @Size(max = 50) String email) {
        this.email = email;
    }

    public @NotNull String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotNull @Size(max = 100) String getAddress() {
        return address;
    }

    public void setAddress(@NotNull @Size(max = 100) String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "ClientDto{" +
                "firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
