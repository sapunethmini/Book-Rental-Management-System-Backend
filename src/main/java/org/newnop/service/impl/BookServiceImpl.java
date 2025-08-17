package org.newnop.service.impl;
import org.newnop.dto.BookDTO;
import org.newnop.entity.Books;
import org.newnop.repository.BooksRepository;
import org.newnop.service.BookServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookServiceInterface {

    @Autowired
    private BooksRepository booksRepository;

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        Books book = new Books();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setGenre(bookDTO.getGenre());
        book.setAvailable(bookDTO.getAvailable() != null ? bookDTO.getAvailable() : true);

        Books savedBook = booksRepository.save(book);
        return convertToDTO(savedBook);
    }

    @Override
    public List<BookDTO> getAllBooks() {
        List<Books> books = booksRepository.findAll();
        return books.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getAvailableBooks() {
        List<Books> books = booksRepository.findByAvailableTrue();
        return books.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public BookDTO getBookById(Long id) {
        Optional<Books> book = booksRepository.findById(id);
        return book.map(this::convertToDTO).orElse(null);
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Optional<Books> optionalBook = booksRepository.findById(id);
        if (optionalBook.isPresent()) {
            Books book = optionalBook.get();

            if (bookDTO.getTitle() != null) {
                book.setTitle(bookDTO.getTitle());
            }
            if (bookDTO.getAuthor() != null) {
                book.setAuthor(bookDTO.getAuthor());
            }
            if (bookDTO.getGenre() != null) {
                book.setGenre(bookDTO.getGenre());
            }
            if (bookDTO.getAvailable() != null) {
                book.setAvailable(bookDTO.getAvailable());
            }

            Books updatedBook = booksRepository.save(book);
            return convertToDTO(updatedBook);
        }
        return null;
    }

    @Override
    public boolean deleteBook(Long id) {
        if (booksRepository.existsById(id)) {
            booksRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<BookDTO> searchBooks(String title, String author, String genre) {
        List<Books> books;

        if (title != null && !title.isEmpty()) {
            books = booksRepository.findByTitleContainingIgnoreCase(title);
        } else if (author != null && !author.isEmpty()) {
            books = booksRepository.findByAuthorContainingIgnoreCase(author);
        } else if (genre != null && !genre.isEmpty()) {
            books = booksRepository.findByGenreContainingIgnoreCase(genre);
        } else {
            books = booksRepository.findAll();
        }

        return books.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Private helper method
    private BookDTO convertToDTO(Books book) {
        return new BookDTO(
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getAvailable()
        );
    }
}