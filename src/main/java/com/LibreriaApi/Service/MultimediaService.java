package com.LibreriaApi.Service;


import com.LibreriaApi.Model.Multimedia;
import com.LibreriaApi.Repository.MultimediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MultimediaService {

    @Autowired
    private MultimediaRepository multimediaRepository;

    // Obtener todos los productos multimedia
    public List<Multimedia> getAllMultimedia() {
        return multimediaRepository.findAll();
    }

    // Obtener un producto multimedia por su ID
    public Optional<Multimedia> getMultimediaById(Long id) {
        return multimediaRepository.findById(id);
    }

    // Crear un nuevo producto multimedia
    public Multimedia createMultimedia(Multimedia multimedia) {
        return multimediaRepository.save(multimedia);
    }

    // Actualizar un producto multimedia existente
    public Multimedia updateMultimedia(Long id, Multimedia multimedia) {
        if (multimediaRepository.existsById(id)) {
            multimedia.setId(id);
            return multimediaRepository.save(multimedia);
        }
        return null;
    }

    // Eliminar un producto multimedia por su ID
    public boolean deleteMultimedia(Long id) {
        if (multimediaRepository.existsById(id)) {
            multimediaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}