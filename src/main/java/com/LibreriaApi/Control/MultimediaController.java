package com.LibreriaApi.Control;

import com.LibreriaApi.Model.Multimedia;
import com.LibreriaApi.Service.MultimediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/multimedia")
public class MultimediaController {

    @Autowired
    private MultimediaService multimediaService;

    // Obtener todos los productos multimedia
    @GetMapping
    public List<Multimedia> getAllMultimedia() {
        return multimediaService.getAllMultimedia();
    }

    // Obtener un producto multimedia por ID
    @GetMapping("/{id}")
    public ResponseEntity<Multimedia> getMultimediaById(@PathVariable Long id) {
        Optional<Multimedia> multimedia = multimediaService.getMultimediaById(id);
        if (multimedia.isPresent()) {
            return ResponseEntity.ok(multimedia.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // Crear un nuevo producto multimedia
    @PostMapping
    public ResponseEntity<Multimedia> createMultimedia(@RequestBody Multimedia multimedia) {
        Multimedia createdMultimedia = multimediaService.createMultimedia(multimedia);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMultimedia);
    }

    // Actualizar un producto multimedia
    @PutMapping("/{id}")
    public ResponseEntity<Multimedia> updateMultimedia(@PathVariable Long id, @RequestBody Multimedia multimedia) {
        Multimedia updatedMultimedia = multimediaService.updateMultimedia(id, multimedia);
        if (updatedMultimedia != null) {
            return ResponseEntity.ok(updatedMultimedia);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // Eliminar un producto multimedia
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMultimedia(@PathVariable Long id) {
        if (multimediaService.deleteMultimedia(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Producto multimedia eliminado");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto multimedia no encontrado");
    }
}