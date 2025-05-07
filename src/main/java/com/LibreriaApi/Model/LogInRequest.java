package com.LibreriaApi.Model;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class LogInRequest {

    @Email
    private String email;
    private String password;

}
