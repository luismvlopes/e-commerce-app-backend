package com.youtube.e_commerce_backend.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private String token;
    private boolean success;
    private String failureMessage;


}
