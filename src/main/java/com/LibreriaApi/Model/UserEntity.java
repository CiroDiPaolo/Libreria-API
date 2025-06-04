package com.LibreriaApi.Model;

import com.LibreriaApi.Enums.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "User")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "idUser")
    private Long id;

    @NotBlank(message = "El nombre de usuario no puede ser nulo")
    @Size(min = 2, max = 20, message = "El usuario debe tener entre 2 y 20 caracteres")
    @Column( name = "username", length = 20)
    private String username;

    @NotBlank(message = "La contraseña no puede ser nula")
    //@Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "La contraseña debe tener al menos una mayúscula, una minúscula y un número")
    @Column( name = "pass")
    private String pass;

    @NotNull(message = "El email no puede ser nulo")
    @Email(message = "El email debe ser valido")
    @Size(max = 30, message = "El email no debe exceder los 30 caracteres")
    @Column( name = "email", length = 30)
    private String email;

    @NotNull(message = "El rol no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<BookStage> favoriteList;


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", pass='" + pass + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
