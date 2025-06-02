package com.LibreriaApi.Service;

import com.LibreriaApi.Enums.Category;
import com.LibreriaApi.Exceptions.EntityNotFoundException;
import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.BookDTO;
import com.LibreriaApi.Repository.BookRepository;
import com.LibreriaApi.Service.GoogleBooksApi.GoogleBooksRequeast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class testBookService {

    // DEPENDENCIAS SIMULADA CON MOCKITO
    @Mock
    private BookRepository bookRepository;
    @Mock
    private GoogleBooksRequeast googleApi;
    // SERVICE QUE QUIERO TESTEAR
    @InjectMocks
    private BookCrudService bookCrudService;

    // OBJETOS DE PRUEBA (LUEGO LOS PODRIA SEPARAR EN UN DataProvider)
    private Book book1;
    private Book book2;
    private BookDTO bookDTO;


    // SETEO LOS DATOS DE LOS OBJETOS DE PRUEBA
    @BeforeEach
    void setUp() throws ParseException {
        book1 = new Book();
        book1.setId(1L);
        book1.setTitle("El Quijote");
        book1.setAuthor("Miguel de Cervantes");
        book1.setISBN("9788408059370");
        book1.setPublishingHouse("Planeta");
        book1.setCategory(Category.NOVELA);
        book1.setDescription("Una obra maestra de la literatura");
        String fechaString = "1605-01-01";
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaBook1 = formato.parse(fechaString);
        book1.setReleaseDate(fechaBook1);
        book1.setStatus(true);
        book1.setUrlImage("http://example.com/quijote.jpg");

        book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Cien años de soledad");
        book2.setAuthor("Gabriel García Márquez");
        book2.setISBN("9788408059370"); // DESPUES PONER EL ISBN QUE CORRESPONDE, LE DEJE EL DEL QUIJOTE PARA PROBAR

        bookDTO = new BookDTO();
        bookDTO.setTitle("Nuevo Libro");
        bookDTO.setAuthor("Nuevo Autor");
        bookDTO.setISBN("978-84-376-0496-1");
        bookDTO.setPublishingHouse("Editorial Test");
        bookDTO.setCategory(Category.NOVELA);
        bookDTO.setDescription("Descripción del libro");
        String fechaStr = "2024-01-01";
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaBook2 = formato.parse(fechaString);
        bookDTO.setReleaseDate(fechaBook2);
        bookDTO.setStatus(true);
    }

    // TESTS PARA MÉTODOS GET //////////////////////////

    // OBTENER UN LIBRO QUE EXISTE
    @Test
    void getBookService_WhenExists_ShouldReturnBook() {
        // Given
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.of(book1));

        // When
        Book result = bookCrudService.getBookService(id);

        // Then
        assertNotNull(result);
        assertEquals(book1, result);
        assertEquals("El Quijote", result.getTitle());
        verify(bookRepository, times(1)).findById(id);
    }

    // OBTENER UN LIBRO QUE NO EXISTE
    @Test
    void getBookService_WhenNotExists_ShouldThrowException() {
        // Given
        Long id = 99L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookCrudService.getBookService(id)
        );

        assertEquals("Libro no encontrado con id: " + id, exception.getMessage());
        verify(bookRepository, times(1)).findById(id);
    }

    // OBTENER TODOS LOS LIBROS
    @Test
    void getAllBooksService_ShouldReturnAllBooks() {
        // Given
        List<Book> expectedBooks = Arrays.asList(book1, book2);
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        // When
        Iterable<Book> result = bookCrudService.getAllBooksService();

        // Then
        assertNotNull(result);
        assertEquals(expectedBooks, result);
        verify(bookRepository, times(1)).findAll();
    }

    // OBTENER LIBRO POR UN TITULO QUE EXISTE
    @Test
    void getBooksByTitleService_WhenExists_ShouldReturnBook() {
        // Given
        String title = "El Quijote";
        when(bookRepository.findByTitle(title)).thenReturn(Optional.of(book1));

        // When
        Optional<Book> result = bookCrudService.getBooksByTitleService(title);

        // Then
        assertTrue(result.isPresent());
        assertEquals(book1, result.get());
        verify(bookRepository, times(1)).findByTitle(title);
    }

    // OBTENER LIBRO POR UN ISBN QUE EXISTE
    @Test
    void getBooksByISBNService_WhenExists_ShouldReturnBook() {
        // Given
        String isbn = "9788408059370";
        when(bookRepository.findByISBN(isbn)).thenReturn(Optional.of(book1));

        // When
        Optional<Book> result = bookCrudService.getBooksByISBNService(isbn);

        // Then
        assertTrue(result.isPresent());
        assertEquals(book1, result.get());
        verify(bookRepository, times(1)).findByISBN(isbn);
    }

    // OBTENER LIBRO POR UN AUTOR QUE EXISTE
    @Test
    void getBooksByAutorService_WhenExists_ShouldReturnBook() {
        // Given
        String author = "Miguel de Cervantes";
        when(bookRepository.findByAuthor(author)).thenReturn(Optional.of(book1));

        // When
        Optional<Book> result = bookCrudService.getBooksByAutorService(author);

        // Then
        assertTrue(result.isPresent());
        assertEquals(book1, result.get());
        verify(bookRepository, times(1)).findByAuthor(author);
    }

    // OBTENER LIBRO POR UNA EDITORIAL QUE EXISTE
    @Test
    void getBooksByEditorialService_WhenExists_ShouldReturnBook() {
        // Given
        String editorial = "Planeta";
        when(bookRepository.findBypublishingHouse(editorial)).thenReturn(Optional.of(book1));

        // When
        Optional<Book> result = bookCrudService.getBooksByEditorialService(editorial);

        // Then
        assertTrue(result.isPresent());
        assertEquals(book1, result.get());
        verify(bookRepository, times(1)).findBypublishingHouse(editorial);
    }

}
