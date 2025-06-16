package com.LibreriaApi.Model.DTO;

import com.LibreriaApi.Model.BookStage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserEntityDTO {

    @NotBlank(message = "El nombre de usuario no puede ser nulo")
    @Size(min = 2, max = 20, message = "El usuario debe tener entre 2 y 20 caracteres")
    private String username;

    @NotBlank(message = "La contraseña no puede ser nula")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @NotBlank(message = "La contraseña no puede ser nula")
    //@Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "La contraseña debe tener al menos una mayúscula, una minúscula y un número")
    private String password;

}
