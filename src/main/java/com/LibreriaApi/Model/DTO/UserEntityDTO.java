package com.LibreriaApi.Model.DTO;

import com.LibreriaApi.Model.BookStage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntityDTO {

    @NotBlank(message = "El nombre de usuario no puede ser nulo")
    @Size(min = 2, max = 20, message = "El usuario debe tener entre 2 y 20 caracteres")
    private String username;

    @NotBlank(message = "La contraseña no puede ser nula")
    //@Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "La contraseña debe tener al menos una mayúscula, una minúscula y un número")
    private String email;

    private List<BookStage> favoriteList;

}
