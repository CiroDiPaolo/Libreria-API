package com.LibreriaApi.Service;

import com.LibreriaApi.Enums.Category;
import com.LibreriaApi.Exceptions.EntityAlreadyExistsException;
import com.LibreriaApi.Exceptions.EntityNotFoundException;
import com.LibreriaApi.Exceptions.ExternalBookNotFoundException;
import com.LibreriaApi.Mapper.ReviewMapper;
import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.DTO.BookDTO;
import com.LibreriaApi.Model.DTO.BookWithReviewsDTO;
import com.LibreriaApi.Model.DTO.LoadBookDTO;
import com.LibreriaApi.Model.DTO.ReviewDTO;
import com.LibreriaApi.Model.Review;
import com.LibreriaApi.Repository.BookRepository;
import com.LibreriaApi.Service.GoogleBooksApi.GoogleBooksRequeast;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GoogleBooksRequeast googleApi;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ReviewMapper reviewMapper;

    //METODOS GET

    public Page<Book> searchByTitlePaged(String title, int page, int size) {
        return bookRepository.findByTitleContainingIgnoreCase(title, PageRequest.of(page, size));
    }

    public Page<Book> searchByContentPaged(String term, int page, int size) {
        return bookRepository.findByReviewContent(term, PageRequest.of(page, size));

    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Libro no encontrado con id: " + id));
    }

    public BookDTO getBooksheet(Long id) {
        return bookRepository.findBookSheetById(id)
                .orElseThrow(() -> new EntityNotFoundException("Libro no encontrado con id: " + id));
    }

    public BookWithReviewsDTO getBookWithReviews(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Libro no encontrado con id: " + id));

        return this.toBookWithReviewsDTO(book);
    }

    public Page<Book> getAllBooks(Pageable pageable) { return bookRepository.findAll(pageable); }

    public Page<Book> searchAdmin(Integer page, Integer size,
                                  String title, String author, String category, Boolean active) {
        int pageNumber = page != null && page >= 0 ? page : 0;
        int pageSize = size != null && size > 0 ? size : 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());

        return bookRepository.searchAdmin(
                nullIfBlank(title),
                nullIfBlank(author),
                nullIfBlank(category),
                active,
                pageable
        );
    }

    private String nullIfBlank(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }

    public Page<BookDTO> getAllActiveBooks(Pageable pageable){return bookRepository.findAllActiveBookDTOs(pageable);}
    public List<Book> getBooksByTitle(String title) { return bookRepository.searchByTitleLikeIgnoreCase(title); }

    public Book getBooksByISBN(String isbn) { return bookRepository.findByISBN(isbn)
            .orElseThrow(()-> new EntityNotFoundException("El libro con ISBN " + isbn + " no existe.")); }

    public List<Book> getBooksByAuthor(String author) { return bookRepository.searchByAuthorLikeIgnoreCase(author); }

    public Page<Book> searchBooks(String title, String author, Category category, String publishingHouse,
                                  Integer fromYear, Integer toYear, Pageable pageable) {

        String t = (title == null || title.isBlank()) ? null : title.trim();
        String a = (author == null || author.isBlank()) ? null : author.trim();
        Category c = category;
        String p = (publishingHouse == null || publishingHouse.isBlank()) ? null : publishingHouse.trim();

        return bookRepository.search(t, a, c, p, fromYear, toYear, pageable);
    }

    public List<Book> getBooksByPublishingHouse(String publishingHouse) { return bookRepository.searchByPublishinHouseLikeIgnoreCase(publishingHouse); }

    //METODOS DELETE
    @Transactional
    public void deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.logicallyDeleteById(id);
        } else {
            throw new EntityNotFoundException("El libro no existe");
        }
    }

    //METODO ADD
    @Transactional
    public Book addBook(BookDTO dto) {
        if(bookRepository.existsByISBN(dto.getISBN())){
            throw new EntityAlreadyExistsException("Ya existe un libro con el ISBN: " + dto.getISBN());
        }
        String url = googleApi.getThumbnailByISBN(dto.getISBN());

        Book book = new Book();

        book.setISBN(dto.getISBN());
        book.setAuthor(dto.getAuthor());
        book.setPublishingHouse(dto.getPublishingHouse());
        book.setTitle(dto.getTitle());
        book.setCategory(dto.getCategory());
        book.setDescription(dto.getDescription());
        book.setReleaseDate(dto.getReleaseDate());
        book.setStatus(dto.getStatus());
        book.setUrlImage(url);

        return bookRepository.save(book);

    }

    // USA UN LoadBookDTO PARA COMPLETAR EL RESTO DE LOS DATOS CON LA API
    // OBTIENE TODOS LOS DATOS EN BASE AL ISBN
    @Transactional
    public Book addBookWithAPI(LoadBookDTO dto) {
        // VALIDO QUE EL ISBN NO ESTE REGISTRADO AÚN
        if(bookRepository.existsByISBN(dto.getIsbn())){
            throw new EntityAlreadyExistsException("Ya existe un libro con el ISBN: " + dto.getIsbn());
        }
        // TRAIGO UN BookInfo, QUE CONTIENE LA INFORMACION OBTENIDA DE LA API
        Optional<GoogleBooksRequeast.BookInfo> bookInfoOpt = googleApi.getBookInfoByISBN(dto.getIsbn());
        // SI EL BookInfo ESTA VACIO EL METODO ARROJA UNA EXCEPCION
        if (bookInfoOpt.isEmpty()) {
            throw new ExternalBookNotFoundException("No se encontró información en Google Books para el ISBN: " + dto.getIsbn());
        }

        // OBTENGO LA URL DE LA PORTADA
        String url = googleApi.getThumbnailByISBN(dto.getIsbn());
        GoogleBooksRequeast.BookInfo info = bookInfoOpt.get();

        Book book = new Book();
        try {
            // SETEO LOS DATOS QUE VIENEN DEL DTO
            book.setISBN(dto.getIsbn());
            book.setCategory(Category.valueOf(dto.getCategory().toUpperCase()));
            book.setAuthor(dto.getAuthor());
            book.setDescription(dto.getDescription());

            // SETEO LOS DATOS QUE OBTENGO CON LA API
            book.setTitle(info.getTitle());
            book.setPublishingHouse(info.getPublisher());

            if (info.getDate() != null || !info.getDate().trim().isEmpty()) {
                book.setReleaseDate(Date.valueOf(info.getDate()));
            }

            book.setUrlImage(url);
            // SETEO EL ESTADO
            book.setStatus(true);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error al completar con la informacion de la API");
        }

        try {
            return bookRepository.save(book);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Error de integridad de datos al guardar el libro", e);
        }
    }

    //METODO UPDATE
    @Transactional
    public Book updateBook(Long idBook, BookDTO bookDTO) {

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("Libro no encontrada con id: " + idBook));

        book.setAuthor(bookDTO.getAuthor());
        book.setTitle(bookDTO.getTitle());
        book.setCategory(bookDTO.getCategory());
        book.setDescription(bookDTO.getDescription());
        book.setPublishingHouse(bookDTO.getPublishingHouse());
        book.setReleaseDate(bookDTO.getReleaseDate());
        book.setStatus(bookDTO.getStatus());
        return book;
    }

    public BookWithReviewsDTO toBookWithReviewsDTO(Book book) {
        List<ReviewDTO> reviewDTOs = book.getReviews().stream()
                .filter(Review::getStatus)
                .map(review -> reviewMapper.toDTO(review))
                .toList();

        BookWithReviewsDTO dto = new BookWithReviewsDTO();
        dto.setId(book.getId());
        dto.setISBN(book.getISBN());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPublishingHouse(book.getPublishingHouse());
        dto.setCategory(book.getCategory());
        dto.setDescription(book.getDescription());
        dto.setReleaseDate(book.getReleaseDate());
        dto.setStatus(book.getStatus());
        dto.setUrlImage(book.getUrlImage());
        dto.setReviewsDTO(reviewDTOs);
        dto.setReviews(null);
        return dto;
    }
}
