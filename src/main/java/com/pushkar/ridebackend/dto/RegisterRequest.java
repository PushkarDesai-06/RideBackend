package com.pushkar.ridebackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @Size(min = 3, max = 25, message = "Minimum required size is 3 and maximum allowed is 50")
    @NotBlank(message = "Username cannot be left blank")
    private String username;


    @Size(min = 6, message = "Minimum required length is 6")
    @NotBlank(message = "Password cannot be left blank")
    private String password;

    @Pattern(regexp = "ROLE_(USER|ADMIN|DRIVER)")
    private String role;

    public RegisterRequest() {}

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
