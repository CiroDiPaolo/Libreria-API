package com.LibreriaApi.Control;

import com.LibreriaApi.Model.Multimedia;
import com.LibreriaApi.Service.MultimediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/multimedia")
@Tag(name = "Multimedia", description = "Operaciones sobre multimedia")
public class MultimediaController {

    @Autowired
    private MultimediaService multimediaService;

    @Operation(summary = "Obtener todos los productos multimedia")
    @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito")
    @GetMapping
    public List<Multimedia> getAllMultimedia() {
        return multimediaService.getAllMultimedia();
    }

    @Operation(summary = "Obtener un producto multimedia por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Multimedia> getMultimediaById(
            @Parameter(description = "ID del producto multimedia") @PathVariable Long id) {
        Optional<Multimedia> multimedia = multimediaService.getMultimediaById(id);
        return multimedia.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @Operation(summary = "Crear un nuevo producto multimedia")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
    @PostMapping
    public ResponseEntity<Multimedia> createMultimedia(
            @Parameter(description = "Datos del nuevo producto multimedia") @RequestBody Multimedia multimedia) {
        Multimedia createdMultimedia = multimediaService.createMultimedia(multimedia);
        return ResponseEntity.status(201).body(createdMultimedia);
    }

    @Operation(summary = "Actualizar un producto multimedia existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Multimedia> updateMultimedia(
            @Parameter(description = "ID del producto a actualizar") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del producto") @RequestBody Multimedia multimedia) {
        Multimedia updatedMultimedia = multimediaService.updateMultimedia(id, multimedia);
        if (updatedMultimedia != null) {
            return ResponseEntity.ok(updatedMultimedia);
        }
        return ResponseEntity.status(404).body(null);
    }

    @Operation(summary = "Eliminar un producto multimedia por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMultimedia(
            @Parameter(description = "ID del producto multimedia a eliminar") @PathVariable Long id) {
        if (multimediaService.deleteMultimedia(id)) {
            return ResponseEntity.status(204).body("Producto multimedia eliminado");
        }
        return ResponseEntity.status(404).body("Producto multimedia no encontrado");
    }
}
