package com.LibreriaApi.Model;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpRequest {

    @NotBlank(message = "El nombre de usuario no puede ser nulo")
    @Size(min = 2, max = 20, message = "El usuario debe tener entre 2 y 20 caracteres")
    private String username;

    @NotNull(message = "El email no puede ser nulo")
    @Email(message = "El email debe ser valido")
    @Size(max = 30, message = "El email no debe exceder los 30 caracteres")
    private String email;

    @NotNull(message = "La contraseña no puede ser nula")
    @Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "La contraseña debe tener al menos una mayúscula, una minúscula y un número")
    private String password;

}
