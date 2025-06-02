package com.LibreriaApi.Service;

import com.LibreriaApi.Enums.Category;
import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.BookDTO;
import com.LibreriaApi.Repository.BookRepository;
import com.LibreriaApi.Service.GoogleBooksApi.GoogleBooksRequeast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

}
