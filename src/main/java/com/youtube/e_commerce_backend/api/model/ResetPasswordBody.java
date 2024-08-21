package com.youtube.e_commerce_backend.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ResetPasswordBody {


    @NotNull
    @NotBlank
    private String token;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z]).{6,}$")
    @Size(min = 6, max = 32)

    private String password;
}
