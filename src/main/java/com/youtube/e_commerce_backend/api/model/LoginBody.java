package com.youtube.e_commerce_backend.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginBody {

    @NotNull
    @NotBlank
    public String username;

    @NotNull
    @NotBlank
    public String password;
}
