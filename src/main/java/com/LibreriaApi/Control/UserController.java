package com.LibreriaApi.Control;

import com.LibreriaApi.Model.DTO.UserEntityDTO;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "Users", description = "Operaciones sobre users")
public class UserController {

    @Autowired
    private UserService userService;

    //metodos get
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{idUser}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long idUser) {

        return ResponseEntity.ok(userService.getUserById(idUser));

    }

    @Operation(
            summary = "Obtiene la informacion del usuario logueado",
            description = "Devuelve los datos del usuario actualmente autenticado, incluyendo su lista de favoritos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informacion del usuario logueado",
                    content = @Content(schema = @Schema(implementation = UserEntity.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping()
    public ResponseEntity<UserEntity> getUserLogged(){

        System.out.println(userService.getIdUserByToken());

        return ResponseEntity.ok(userService.getUserById(userService.getIdUserByToken()));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<UserEntity> getAllUsers(){

        return ResponseEntity.ok(userService.getAllUsers()).getBody();

    }

    //METODO DELETE
    @Operation(
            summary = "Da de baja un usuario elegido",
            description = "Permite a un usuario con rol de ADMIN desactivar un usuario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario dado de baja",
                    content = @Content(schema = @Schema(implementation = UserEntity.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "El usuario ya estaba inactivo"),
            @ApiResponse(responseCode = "403", description = "No autorizado (no tiene rol ADMIN)")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{idUser}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long idUser){

        userService.deleteUserById(idUser);

        return ResponseEntity.noContent().build();

    }

    //metodo post
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping()
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntityDTO user){

        return ResponseEntity.ok(userService.updateUser(user));

    }

    @Operation(
            summary = "Activa un usuario previamente desactivado",
            description = "Se necesita el rol ADMIN para dar de alta al usuario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario activado correctamente",
                    content = @Content(schema = @Schema(implementation = UserEntity.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "El usuario ya estaba activo"),
            @ApiResponse(responseCode = "403", description = "No autorizado (no tiene rol ADMIN)")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/activate/{id}")
    public ResponseEntity<UserEntity> activateUser(@PathVariable Long id){

        UserEntity user = userService.activateUserById(id);

        return ResponseEntity.ok(user);

    }

}
