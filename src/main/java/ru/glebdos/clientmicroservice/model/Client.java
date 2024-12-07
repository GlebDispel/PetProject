package ru.glebdos.clientmicroservice.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "customer", schema = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "first_name")
    @NotNull
    @Size(min = 2, max = 50)
    private  String firstName;
    @Column(name = "second_name")
    @NotNull
    @Size(min = 2, max = 50)
    private  String secondName;
    @Column(name = "email")
    @NotNull
    @Email
    @Size(max = 50)
    private  String email;
    @Column(name = "phone_number")
    @Pattern(regexp = "^\\+7[0-9]{10}$", message = "Неправильный формат телефонного номера. Ожидаемый формат: +79219008833")
    @NotNull
    private  String phoneNumber;
    @Column(name = "address")
    @NotNull
    @Size(max = 100)
    private  String address;
    @NotNull
    @Column(name = "registration_time")
    private LocalDateTime clientRegistrationTime;

    public Client(String firstName, String secondName, String email, String phoneNumber, String address) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
    public Client() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public @NotNull @Email @Size(max = 50) String getEmail() {
        return email;
    }

    public void setEmail(@NotNull @Email @Size(max = 50) String email) {
        this.email = email;
    }

    public @Pattern(regexp = "^\\+7[0-9]{10}$", message = "Неправильный формат телефонного номера. Ожидаемый формат: +79219008833") @NotNull String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@Pattern(regexp = "^\\+7[0-9]{10}$", message = "Неправильный формат телефонного номера. Ожидаемый формат: +79219008833") @NotNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotNull @Size(max = 100) String getAddress() {
        return address;
    }

    public void setAddress(@NotNull @Size(max = 100) String address) {
        this.address = address;
    }

    public @NotNull LocalDateTime getClientRegistrationTime() {
        return clientRegistrationTime;
    }

    public void setClientRegistrationTime(@NotNull LocalDateTime clientRegistrationTime) {
        this.clientRegistrationTime = clientRegistrationTime;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", clientRegistrationTime=" + clientRegistrationTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && Objects.equals(firstName, client.firstName) && Objects.equals(secondName, client.secondName) && Objects.equals(email, client.email) && Objects.equals(phoneNumber, client.phoneNumber) && Objects.equals(address, client.address) && Objects.equals(clientRegistrationTime, client.clientRegistrationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, secondName, email, phoneNumber, address, clientRegistrationTime);
    }
}
