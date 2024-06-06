package com.youtube.e_commerce_backend.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginBody {

    @NotNull
    @NotBlank
    public String username;

    @NotNull
    @NotBlank
    public String password;
}
