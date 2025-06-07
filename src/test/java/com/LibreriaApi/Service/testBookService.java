package com.LibreriaApi.Service;

import com.LibreriaApi.Enums.Category;
import com.LibreriaApi.Exceptions.EntityNotFoundException;
import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.DTO.BookDTO;
import com.LibreriaApi.Model.DTO.BookWithReviewsDTO;
import com.LibreriaApi.Model.DTO.ReviewDTO;
import com.LibreriaApi.Model.Review;
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
import java.time.LocalDate;
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
    @Mock
    private ReviewService reviewService;
    // SERVICE QUE QUIERO TESTEAR
    @InjectMocks
    private BookService bookService;

    // OBJETOS DE PRUEBA (LUEGO LOS PODRIA SEPARAR EN UN DataProvider)
    private Book book;
    private BookDTO bookDTO;
    private Review review;
    private ReviewDTO reviewDTO;

    @BeforeEach
    void setUp() throws ParseException{
        // Configuro libro de prueba
        book = new Book();
        book.setId(1L);
        book.setISBN("978-3-16-148410-0");
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setPublishingHouse("Test Publisher");
        book.setCategory(Category.CIENCIA_FICCION);
        book.setDescription("Test Description");
        String fechaString = "2024-06-09";
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaBook = formato.parse(fechaString);
        book.setStatus(true);
        book.setUrlImage("http://test-image.com");

        // Configuro DTO de prueba
        bookDTO = new BookDTO();
        bookDTO.setISBN("978-3-16-148410-0");
        bookDTO.setTitle("Test Book");
        bookDTO.setAuthor("Test Author");
        bookDTO.setPublishingHouse("Test Publisher");
        bookDTO.setCategory(Category.CIENCIA_FICCION);
        bookDTO.setDescription("Test Description");
        String fechaStringDTO = "1605-01-01";
        SimpleDateFormat formatoDTO = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaBookDTO = formato.parse(fechaString);
        bookDTO.setStatus(true);

        // Configuro review de prueba
        review = new Review();
        review.setIdReview(1L);
        review.setRating(5);
        review.setContent("Good book");
        review.setStatus(true);

        // Configuro ReviewDTO
        reviewDTO = new ReviewDTO(
                1L,
                5,
                "Good book",
                true,
                1L,
                1L

        );
        // Agrego la review al libro
        book.setReviews(Arrays.asList(review));

    }

    // SETEO LOS DATOS DE LOS OBJETOS DE PRUEBA
//    @BeforeEach
//    void setUp() throws ParseException {
//        book1 = new Book();
//        book1.setId(1L);
//        book1.setTitle("El Quijote");
//        book1.setAuthor("Miguel de Cervantes");
//        book1.setISBN("9788408059370");
//        book1.setPublishingHouse("Planeta");
//        book1.setCategory(Category.NOVELA);
//        book1.setDescription("Una obra maestra de la literatura");
//        String fechaString = "1605-01-01";
//        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
//        Date fechaBook1 = formato.parse(fechaString);
//        book1.setReleaseDate(fechaBook1);
//        book1.setStatus(true);
//        book1.setUrlImage("http://example.com/quijote.jpg");
//
//        book2 = new Book();
//        book2.setId(2L);
//        book2.setTitle("Cien años de soledad");
//        book2.setAuthor("Gabriel García Márquez");
//        book2.setISBN("9788408059370"); // DESPUES PONER EL ISBN QUE CORRESPONDE, LE DEJE EL DEL QUIJOTE PARA PROBAR
//
//        bookDTO = new BookDTO();
//        bookDTO.setTitle("Nuevo Libro");
//        bookDTO.setAuthor("Nuevo Autor");
//        bookDTO.setISBN("978-84-376-0496-1");
//        bookDTO.setPublishingHouse("Editorial Test");
//        bookDTO.setCategory(Category.NOVELA);
//        bookDTO.setDescription("Descripción del libro");
//        String fechaStr = "2024-01-01";
//        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
//        Date fechaBook2 = formato.parse(fechaString);
//        bookDTO.setReleaseDate(fechaBook2);
//        bookDTO.setStatus(true);
//    }

    // TESTS PARA MÉTODOS GET //////////////////////////

    @Test
    void getBookByIdService_WhenBookExists_ShouldReturnBook() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // When
        Book result = bookService.getBookByIdService(1L);

        // Then
        assertNotNull(result);
        assertEquals(book.getId(), result.getId());
        assertEquals(book.getTitle(), result.getTitle());
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookByIdService_WhenBookNotExists_ShouldThrowException() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.getBookByIdService(1L)
        );
        assertEquals("Libro no encontrado con id: 1", exception.getMessage());
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookWithReviewsService_WhenBookExists_ShouldReturnBookWithReviewsDTO() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(reviewService.toDTO(review)).thenReturn(reviewDTO);

        // When
        BookWithReviewsDTO result = bookService.getBookWithReviewsService(1L);

        // Then
        assertNotNull(result);
        assertEquals(book.getId(), result.getId());
        assertEquals(book.getTitle(), result.getTitle());
        assertNotNull(result.getReviewsDTO());
        assertEquals(1, result.getReviewsDTO().size());
        verify(bookRepository).findById(1L);
        verify(reviewService).toDTO(review);
    }

    @Test
    void getBookWithReviewsService_WhenBookNotExists_ShouldThrowException() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.getBookWithReviewsService(1L)
        );
        assertEquals("Libro no encontrado con id: 1", exception.getMessage());
        verify(bookRepository).findById(1L);
    }

    @Test
    void getAllBooksService_ShouldReturnAllBooks() {
        // Given
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findAll()).thenReturn(books);

        // When
        List<Book> result = bookService.getAllBooksService();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(book, result.get(0));
        verify(bookRepository).findAll();
    }

    @Test
    void getBooksByTitleService_ShouldReturnMatchingBooks() {
        // Given
        String title = "Test";
        List<Book> books = Arrays.asList(book);
        when(bookRepository.searchByTitleLikeIgnoreCase(title)).thenReturn(books);

        // When
        List<Book> result = bookService.getBooksByTitleService(title);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookRepository).searchByTitleLikeIgnoreCase(title);
    }

    @Test
    void getBooksByISBNService_WhenBookExists_ShouldReturnBook() {
        // Given
        String isbn = "978-3-16-148410-0";
        when(bookRepository.findByISBN(isbn)).thenReturn(Optional.of(book));

        // When
        Book result = bookService.getBooksByISBNService(isbn);

        // Then
        assertNotNull(result);
        assertEquals(book.getISBN(), result.getISBN());
        verify(bookRepository).findByISBN(isbn);
    }

    @Test
    void getBooksByISBNService_WhenBookNotExists_ShouldThrowException() {
        // Given
        String isbn = "978-3-16-148410-0";
        when(bookRepository.findByISBN(isbn)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.getBooksByISBNService(isbn)
        );
        assertEquals("El libro con ISBN " + isbn + " no existe.", exception.getMessage());
        verify(bookRepository).findByISBN(isbn);
    }

    @Test
    void getBooksByAutorService_ShouldReturnMatchingBooks() {
        // Given
        String author = "Test Author";
        List<Book> books = Arrays.asList(book);
        when(bookRepository.searchByAuthorLikeIgnoreCase(author)).thenReturn(books);

        // When
        List<Book> result = bookService.getBooksByAutorService(author);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookRepository).searchByAuthorLikeIgnoreCase(author);
    }

    @Test
    void getBooksByPublishingHouseService_ShouldReturnMatchingBooks() {
        // Given
        String publishingHouse = "Test Publisher";
        List<Book> books = Arrays.asList(book);
        when(bookRepository.searchByPublishinHouseLikeIgnoreCase(publishingHouse)).thenReturn(books);

        // When
        List<Book> result = bookService.getBooksByPublishingHouseService(publishingHouse);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookRepository).searchByPublishinHouseLikeIgnoreCase(publishingHouse);
    }

    // TESTS PARA MÉTODO DELETE ////////////////////

    // ELIMINA UN LIBRO QUE EXISTE
    @Test
    void deleteBookService_WhenExists_ShouldDeleteSuccessfully() {
        // Given
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.of(book1));
        // Buscar como funciona el doNothing
        doNothing().when(bookRepository).deleteById(id);

        // When & Then
        assertDoesNotThrow(() -> bookService.deleteBookService(id));

        verify(bookRepository, times(1)).findById(id);
        verify(bookRepository, times(1)).deleteById(id);
    }

    // ELIMINAR UN LIBRO QUE NO EXISTE
    @Test
    void deleteBookService_WhenNotExists_ShouldThrowException() {
        // Given
        Long id = 99L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.deleteBookService(id)
        );

        assertEquals("El libro no existe", exception.getMessage());
        verify(bookRepository, times(1)).findById(id);
        verify(bookRepository, never()).deleteById(anyLong());
    }

    // TESTS PARA MÉTODO ADD ////////////////////////

    // AGREGAR UN LIBRO (COMPRUEBA SI SE CREA JUNTO CON SU IMAGEN)
    @Test
    void addBookService_ShouldCreateBookWithGoogleImage() {
        // Given
        String expectedImageUrl = "http://books.google.com/books/content?id=hKiTPwAACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api";
        when(googleApi.getThumbnailByISBN(bookDTO.getISBN())).thenReturn(expectedImageUrl);
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        // When
        Book result = bookService.addBookService(bookDTO);

        // Then
        assertNotNull(result);
        assertEquals(book1, result);

        verify(googleApi, times(1)).getThumbnailByISBN(bookDTO.getISBN());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    // VALIDA QUE EL DTO SE MAPPEO CORRECTAMENTE, O ALGO ASI
    @Test
    void addBookService_ShouldMapDTOFieldsCorrectly() {
        // Given
        String expectedImageUrl = "http://google-books.com/thumbnail.jpg";
        when(googleApi.getThumbnailByISBN(anyString())).thenReturn(expectedImageUrl);
        // Buscar que hace lo siguiente:
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            savedBook.setId(1L);
            return savedBook;
        });

        // When
        Book result = bookService.addBookService(bookDTO);

        // Then
        assertNotNull(result);
        assertEquals(bookDTO.getTitle(), result.getTitle());
        assertEquals(bookDTO.getAuthor(), result.getAuthor());
        assertEquals(bookDTO.getISBN(), result.getISBN());
        assertEquals(bookDTO.getPublishingHouse(), result.getPublishingHouse());
        assertEquals(bookDTO.getCategory(), result.getCategory());
        assertEquals(bookDTO.getDescription(), result.getDescription());
        assertEquals(bookDTO.getReleaseDate(), result.getReleaseDate());
        assertEquals(bookDTO.getStatus(), result.getStatus());
        assertEquals(expectedImageUrl, result.getUrlImage());
    }

    // TESTS PARA MÉTODO UPDATE /////////////

    // ACTUALIZA UN LIBRO QUE EXISTE
    @Test
    void updateBookService_WhenExists_ShouldUpdateSuccessfully() {
        // Given
        Book bookToUpdate = new Book();
        bookToUpdate.setId(1L);
        bookToUpdate.setTitle("Título Actualizado");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.save(bookToUpdate)).thenReturn(bookToUpdate);

        // When & Then
        assertDoesNotThrow(() -> bookService.updateBookService(bookToUpdate));

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(bookToUpdate);
    }

    // ACTUALIZA UN LIBRO QUE NO EXISTE
    @Test
    void updateBookService_WhenNotExists_ShouldThrowException() {
        // Given
        Book bookToUpdate = new Book();
        bookToUpdate.setId(99L);

        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.updateBookService(bookToUpdate)
        );

        assertEquals("El libro no existe", exception.getMessage());
        verify(bookRepository, times(1)).findById(99L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    // TESTS PARA CASOS EDGE

    // AGREGA UN LIBRO PERO LA API DE GOOGLE DEVUELVE NULL
    @Test
    void addBookService_WhenGoogleApiReturnsNull_ShouldHandleGracefully() {
        // Given
        when(googleApi.getThumbnailByISBN(bookDTO.getISBN())).thenReturn(null);
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        // When
        Book result = bookService.addBookService(bookDTO);

        // Then
        assertNotNull(result);
        verify(googleApi, times(1)).getThumbnailByISBN(bookDTO.getISBN());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

}
