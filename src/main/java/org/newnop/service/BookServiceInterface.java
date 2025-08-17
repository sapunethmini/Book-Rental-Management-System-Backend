package org.newnop.service;
import org.newnop.dto.BookDTO;
import java.util.List;

public interface BookServiceInterface {

    BookDTO createBook(BookDTO bookDTO);

    List<BookDTO> getAllBooks();
    List<BookDTO> getAvailableBooks();
    BookDTO getBookById(Long id);

    BookDTO updateBook(Long id, BookDTO bookDTO);

    boolean deleteBook(Long id);

    List<BookDTO> searchBooks(String title, String author, String genre);
}
