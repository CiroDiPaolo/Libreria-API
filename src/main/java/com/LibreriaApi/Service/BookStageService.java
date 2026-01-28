package com.LibreriaApi.Service;

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
    private BookRepository bookRepository;

    // POST
    @Transactional
    public BookStage createBookStage(Long id) {
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
    public List<BookStage> getAllBookStageOfUser() {
        Long idUser = userService.getIdUserByToken();
        return bookStageRepository.findByUser_IdAndBook_StatusTrue(idUser);
    }

    @Transactional
    public List<BookStage> getAllBookStageOfAUser(Long id) {
        return bookStageRepository.findByUser_IdAndBook_StatusTrue(id);
    }

    // DELETE
    @Transactional
    public void deleteBookStageOfUserById(Long id) {
        Long idUser = userService.getIdUserByToken();
        UserEntity user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con el id: " + idUser));
        // BUSCO EL BookStage QUE EL USUARIO QUIERE ELIMINAR
        BookStage bookStage = bookStageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BookStage no encontrado con el id: " + id));
        // VALIDO QUE EL BookStage PERTENEZCA A LA LISTA DEL USUARIO
        if (!user.getFavoriteList().contains(bookStage)) {
            throw new EntityNotFoundException("El usuario no contiene este libro en favoritos");
        }
        // LO REMUEVO DE LA LISTA
        user.getFavoriteList().remove(bookStage);
        userRepository.save(user);
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
    public BookStage updateBookStage(BookStageDTO dto) {
        Long userId = userService.getIdUserByToken();

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con el id: " + userId));

        // BUSCO EL BookStage QUE COINCIDA CON EL USUARIO Y EL LIBRO
        BookStage bookStage = bookStageRepository.findByUserIdAndBookId(userId, dto.getIdBook())
                .orElseThrow(() -> new BookStageNotFoundException("BookStage no encontrado con el id del libro: " + dto.getIdBook()));

        if (bookStage.getStage() == dto.getStage()) {
            throw new EntityNotFoundException("El stage no fue modificado");
        }

        bookStage.setStage(dto.getStage());

        return bookStageRepository.save(bookStage);
    }

}