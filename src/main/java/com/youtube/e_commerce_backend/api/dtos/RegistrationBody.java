package com.youtube.e_commerce_backend.api.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class RegistrationBody {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 255)
    private String username;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z]).{6,}$")
    @Size(min = 6, max = 32)
    private String password;
    @Email
    @NotNull
    @NotBlank
    private String email;
    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String lastName;
}
