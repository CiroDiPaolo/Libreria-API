package com.LibreriaApi.Service;

import com.LibreriaApi.Exceptions.AccessDeniedUserException;
import com.LibreriaApi.Exceptions.EntityNotFoundException;
import com.LibreriaApi.Mapper.ReviewMapper;
import com.LibreriaApi.Model.DTO.ReviewDTO;
import com.LibreriaApi.Model.Multimedia;
import com.LibreriaApi.Model.Review;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.BookRepository;
import com.LibreriaApi.Repository.MultimediaRepository;
import com.LibreriaApi.Repository.ReviewRepository;
import com.LibreriaApi.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewMapper reviewMapper;

    //Metodos GET

    public ReviewDTO getReviewById(Long id) {

        return reviewMapper.toDTO(reviewRepository.findById(id) .orElseThrow(() -> new EntityNotFoundException("Review no encontrada con id: " + id)));


    }

    public List<ReviewDTO> getAllReviewsOfABook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Libro no encontrado con id: " + bookId);
        }
        return reviewRepository.findByMultimedia_Id(bookId).stream().map(reviewMapper::toDTO).toList();
    }

    public List<ReviewDTO> getAllActiveReviewsOfABook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Libro no encontrado con id: " + bookId);
        }
        return reviewRepository.findByMultimediaIdAndStatusTrue(bookId).stream().map(reviewMapper::toDTO).toList();
    }

    public ReviewDTO getReviewByUserAndBookAndStatusTrue(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Libro no encontrado con id: " + bookId);
        }
        Long idUser = userService.getIdUserByToken();
        Optional<Review> review = reviewRepository.findByMultimediaIdAndUserIdAndStatusTrue(bookId, idUser);
        if (review.isPresent()){
            return reviewMapper.toDTO(review.get());
        }else{
            throw new EntityNotFoundException("El usuario no tiene review del libro " + bookId);
        }
    }

    public ReviewDTO getReviewByUserAndBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Libro no encontrado con id: " + bookId);
        }
        Long idUser = userService.getIdUserByToken();
        Optional<Review> review = reviewRepository.findByMultimediaIdAndUserId(bookId, idUser);
        if (review.isPresent()){
            return reviewMapper.toDTO(review.get());
        }else{
            throw new EntityNotFoundException("El usuario no tiene review del libro " + bookId);
        }
    }

    public List<ReviewDTO> getAllReviewsOfUser(Long idUser) {
        return reviewRepository
                .findReviewActiveOfUserById(idUser)
                .stream()
                .map(reviewMapper::toDTO)
                .toList();
    }

    //Metodos DELETE
    @Transactional
    public void deleteByIdServiceUser(Long idReview) {
        Long idUser = userService.getIdUserByToken();
       if(this.checkReviewBelongsToUser(idUser, idReview)){
           reviewRepository.logicallyDeleteById(idReview);
       }
    }

    @Transactional
    public void deleteById(Long idReview) {
        if(reviewRepository.existsById(idReview)){
            reviewRepository.logicallyDeleteById(idReview);
        }else{
            throw new EntityNotFoundException("La review con id " + idReview + " no existe");
        }
    }

    //Metodo POST
    @Transactional
    public ReviewDTO addReview(ReviewDTO review) {
        Long idUser = userService.getIdUserByToken();
        if(reviewRepository.existsByUser_IdAndStatusTrueAndMultimedia_Id(idUser, review.getIdMultimedia())){
            throw new AccessDeniedUserException("El usuario ya tiene una review activa para este contenido");
        }
        review.setIdUser(idUser);
        review.setStatus(true);
        return reviewMapper.toDTO(reviewRepository.save(this.reviewMapper.toModel(review)));
    }

    //Meteodo PUT

    @Transactional
    public ReviewDTO updateReview(Long idReview, ReviewDTO reviewDTO) {
        Long idUser = userService.getIdUserByToken();
        this.checkReviewBelongsToUser(idUser, idReview);

        Review review = reviewRepository.findById(idReview)
                .orElseThrow(() -> new EntityNotFoundException("Review no encontrada con id: " + idReview));

        if(!review.getStatus()){
            throw new AccessDeniedUserException("La reseña esta eliminada");
        }
        review.setContent(reviewDTO.getContent());
        review.setRating(reviewDTO.getRating());

        return reviewMapper.toDTO(review);
    }

    @Transactional
    public ReviewDTO updateReviewAdmin(Long idReview, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(idReview)
                .orElseThrow(() -> new EntityNotFoundException("Review no encontrada con id: " + idReview));

        review.setStatus(reviewDTO.getStatus());

        return reviewMapper.toDTO(review);
    }

    public boolean checkReviewBelongsToUser(Long idUser, Long idReview){
        Optional<Review> review = reviewRepository.findById(idReview);
        if(review.isPresent()){
            if(Objects.equals(review.get().getUser().getId(), idUser)){
                return true;
            }else{
                throw new AccessDeniedUserException("La review no corresponde a su usuario");
            }
        }else{
            throw new EntityNotFoundException("Review no encontrada id = " + idReview);
        }
    }

}

