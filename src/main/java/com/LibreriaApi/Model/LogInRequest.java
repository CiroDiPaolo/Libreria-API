package com.LibreriaApi.Model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LogInRequest {

    @NotNull(message = "El email no puede ser nulo")
    @Email(message = "El email debe ser valido")
    @Size(max = 30, message = "El email no debe exceder los 30 caracteres")
    private String email;

    @NotNull(message = "La contraseña no puede ser nula")
    @Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "La contraseña debe tener al menos una mayúscula, una minúscula y un número")
    private String password;

}
