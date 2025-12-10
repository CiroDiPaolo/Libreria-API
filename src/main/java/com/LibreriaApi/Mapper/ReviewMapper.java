package com.LibreriaApi.Mapper;

import com.LibreriaApi.Exceptions.EntityNotFoundException;
import com.LibreriaApi.Model.DTO.ReviewDTO;
import com.LibreriaApi.Model.Multimedia;
import com.LibreriaApi.Model.Review;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.MultimediaRepository;
import com.LibreriaApi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    @Autowired
    private MultimediaRepository multimediaRepository;

    @Autowired
    private UserRepository userRepository;

    public ReviewDTO toDTO (Review review){
        return new ReviewDTO(
                review.getIdReview(),
                review.getRating(),
                review.getContent(),
                review.getStatus(),
                review.getUser().getId(),
                review.getMultimedia().getId()
        );
    }

    public Review toModel (ReviewDTO reviewDTO){
        Long idMultimedia = reviewDTO.getIdMultimedia();
        Multimedia multimedia = multimediaRepository.findById(idMultimedia)
                .orElseThrow(() -> new EntityNotFoundException("Multimedia no encontrado con id: " + idMultimedia));

        Long idUser = reviewDTO.getIdUser();
        UserEntity user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + idUser));
        return new Review(
                reviewDTO.getIdReview(),
                reviewDTO.getRating(),
                reviewDTO.getContent(),
                reviewDTO.getStatus(),
                user,
                multimedia
        );
    }
}
