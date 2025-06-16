package com.LibreriaApi.Service;

import com.LibreriaApi.Control.BookCrudController;
import com.LibreriaApi.Enums.Stage;
import com.LibreriaApi.Exceptions.BookStageNotFoundException;
import com.LibreriaApi.Exceptions.EntityNotFoundException;
import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.BookStage;
import com.LibreriaApi.Model.DTO.BookStageDTO;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.BookRepository;
import com.LibreriaApi.Repository.BookStageRepository;
import com.LibreriaApi.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookStageService {

    @Autowired
    private BookStageRepository bookStageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookCrudController bookCrudController;

    @Autowired
    private BookRepository bookRepository;

    // POST
    @Transactional
    public BookStage createService(Long id) {
        Long userId = userService.getIdUserByToken();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con el id: " + userId));
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Libro no encontrado"));

        if (bookStageRepository.findByUserAndBook(user, book).isPresent()) {
            throw new BookStageNotFoundException("Este usuario ya tiene este libro en favoritos");
        }

        BookStage bookStage = new BookStage();
        bookStage.setBook(book);
        bookStage.setUser(user);
        bookStage.setStage(Stage.PENDIENTE);

        return bookStageRepository.save(bookStage);
    }

    // GET
    @Transactional
    public BookStage getBookStageById(Long id) {
        return bookStageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BookStage no encontrado con el id: " + id));
    }

    @Transactional
    public List<BookStage> getAllBookStageOfUserService() {
        Long idUser = userService.getIdUserByToken();
        return bookStageRepository.findByUserId(idUser);
    }

    @Transactional
    public List<BookStage> getAllBookStageOfAUserService(Long id) {
        return bookStageRepository.findByUserId(id);
    }

    // DELETE
    @Transactional
    public void deleteBookStageOfUserById(Long id) {
        Long idUser = userService.getIdUserByToken();
        UserEntity user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con el id: " + idUser));
        BookStage bookStage = bookStageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BookStage no encontrado con el id: " + id));

        if (!user.getFavoriteList().contains(bookStage)) {
            throw new EntityNotFoundException("El usuario no contiene este libro en favoritos");
        }

        user.getFavoriteList().remove(bookStage);
        userRepository.save(user);
    }

    @Transactional
    public void deleteBookStageOfAUserById(Long id) {
        deleteBookStageOfUserById(id);
    }

    @Transactional
    public void deleteBookStageOfAUserByDTO(Long idUser, Long idBook) {
        UserEntity user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con el id: " + idUser));
        BookStage bookStage = bookStageRepository.findByBookId(idBook)
                .orElseThrow(() -> new EntityNotFoundException("BookStage no encontrado con el id del libro: " + idBook));

        if (!user.getFavoriteList().contains(bookStage)) {
            throw new EntityNotFoundException("El usuario no contiene este libro en favoritos");
        }

        user.getFavoriteList().remove(bookStage);
        userRepository.save(user);
    }

    // PUT
    @Transactional
    public BookStage updateBookStage(BookStageDTO bookStageDTO) {
        Long userId = userService.getIdUserByToken();

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con el id: " + bookStageDTO.getIdUser()));

        // BUSCO EL BookStage QUE COINCIDA CON EL USUARIO Y EL LIBRO
        BookStage bookStage = bookStageRepository.findByUserIdAndBookId(userId, bookStageDTO.getIdBook())
                .orElseThrow(() -> new BookStageNotFoundException("BookStage no encontrado con el id del libro: " + bookStageDTO.getIdBook()));

        if (bookStage.getStage() == bookStageDTO.getStage()) {
            throw new EntityNotFoundException("El stage no fue modificado");
        }

        bookStage.setStage(bookStageDTO.getStage());

        return bookStageRepository.save(bookStage);
    }

    /*
    @Transactional
    public BookStage updateBookStage(BookStageDTO bookStageDTO) {
        UserEntity user = userRepository.findById(bookStageDTO.getIdUser())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con el id: " + bookStageDTO.getIdUser()));
        BookStage bookStage = bookStageRepository.findByBookId(bookStageDTO.getIdBook())
                .orElseThrow(() -> new EntityNotFoundException("BookStage no encontrado con el id del libro: " + bookStageDTO.getIdBook()));

        if (!user.getFavoriteList().contains(bookStage)) {
            throw new EntityNotFoundException("El usuario no contiene este libro");
        }

        if (bookStage.getStage() == bookStageDTO.getStage()) {
            throw new EntityNotFoundException("El stage no fue modificado");
        }

        bookStage.setBook(
                bookRepository.findById(bookStageDTO.getIdBook())
                        .orElseThrow(() -> new EntityNotFoundException("Libro no encontrado"))
        );
        bookStage.setStage(bookStageDTO.getStage());
        return bookStageRepository.save(bookStage);
    }
    */

}