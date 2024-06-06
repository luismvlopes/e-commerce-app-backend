package com.youtube.e_commerce_backend.api.controller.auth;

import com.youtube.e_commerce_backend.api.dtos.LoginBody;
import com.youtube.e_commerce_backend.api.dtos.LoginResponse;
import com.youtube.e_commerce_backend.api.dtos.RegistrationBody;
import com.youtube.e_commerce_backend.exception.LoginFailedException;
import com.youtube.e_commerce_backend.exception.UserAlreadyExistsException;
import com.youtube.e_commerce_backend.model.LocalUser;
import com.youtube.e_commerce_backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
        try {
            userService.registerUser(registrationBody);
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody) {
        String jwt = userService.loginUser(loginBody);
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        LoginResponse response = new LoginResponse();
        response.setToken(jwt);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<LocalUser> getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user) {
        return ResponseEntity.ok(user);

    }

}
