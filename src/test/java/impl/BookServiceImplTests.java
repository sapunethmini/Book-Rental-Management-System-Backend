package org.newnop.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.newnop.dto.book.InputBookDTO;
import org.newnop.dto.book.OutputBookDTO;
import org.newnop.entity.Books;
import org.newnop.mapper.BookMapper;
import org.newnop.repository.BooksRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTests {

    @Mock
    private BooksRepository booksRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Books testBook;
    private InputBookDTO testInputDTO;
    private OutputBookDTO testOutputDTO;

    @BeforeEach
    void setUp() {
        testBook = new Books();
        testBook.setBookId(1L);
        testBook.setTitle("Java Programming");
        testBook.setAuthor("John Doe");
        testBook.setGenre("Programming");
        testBook.setAvailable(true);

        testInputDTO = new InputBookDTO();
        testInputDTO.setTitle("Java Programming");
        testInputDTO.setAuthor("John Doe");
        testInputDTO.setGenre("Programming");
        testInputDTO.setAvailable(true);
        
        testOutputDTO = new OutputBookDTO();
        testOutputDTO.setBookId(1L);
        testOutputDTO.setTitle("Java Programming");
        testOutputDTO.setAuthor("John Doe");
        testOutputDTO.setGenre("Programming");
        testOutputDTO.setAvailable(true);
    }

    @Test
    void testCreateBook() {
        when(bookMapper.toBookEntity(testInputDTO)).thenReturn(testBook);
        when(booksRepository.save(testBook)).thenReturn(testBook);
        when(bookMapper.toBookOutput(testBook)).thenReturn(testOutputDTO);

        OutputBookDTO result = bookService.createBook(testInputDTO);

        assertNotNull(result);
        assertEquals(testOutputDTO.getBookId(), result.getBookId());
        assertEquals(testOutputDTO.getTitle(), result.getTitle());
        verify(booksRepository).save(testBook);
        verify(bookMapper).toBookEntity(testInputDTO);
        verify(bookMapper).toBookOutput(testBook);
    }

    @Test
    void testGetAllBooks() {
        List<Books> books = Arrays.asList(testBook);

        when(booksRepository.findAll()).thenReturn(books);
        when(bookMapper.toBookOutput(testBook)).thenReturn(testOutputDTO);

        List<OutputBookDTO> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testOutputDTO.getTitle(), result.get(0).getTitle());
        verify(booksRepository).findAll();
    }

    @Test
    void testGetAvailableBooks() {
        List<Books> availableBooks = Arrays.asList(testBook);

        when(booksRepository.findByAvailableTrue()).thenReturn(availableBooks);
        when(bookMapper.toBookOutput(testBook)).thenReturn(testOutputDTO);

        List<OutputBookDTO> result = bookService.getAvailableBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getAvailable());
        verify(booksRepository).findByAvailableTrue();
    }

    @Test
    void testGetBookById_Found() {
        when(booksRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookMapper.toBookOutput(testBook)).thenReturn(testOutputDTO);

        OutputBookDTO result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getBookId());
        verify(booksRepository).findById(1L);
    }

    @Test
    void testGetBookById_NotFound() {
        when(booksRepository.findById(1L)).thenReturn(Optional.empty());

        OutputBookDTO result = bookService.getBookById(1L);

        assertNull(result);
        verify(booksRepository).findById(1L);
        verify(bookMapper, never()).toBookOutput(any());
    }

    @Test
    void testUpdateBook_Found() {
        when(booksRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(booksRepository.save(testBook)).thenReturn(testBook);
        when(bookMapper.toBookOutput(testBook)).thenReturn(testOutputDTO);

        OutputBookDTO result = bookService.updateBook(1L, testInputDTO);

        assertNotNull(result);
        verify(booksRepository).findById(1L);
        verify(bookMapper).updateBookFromInput(testBook, testInputDTO);
        verify(booksRepository).save(testBook);
    }

    @Test
    void testUpdateBook_NotFound() {
        when(booksRepository.findById(1L)).thenReturn(Optional.empty());

        OutputBookDTO result = bookService.updateBook(1L, testInputDTO);

        assertNull(result);
        verify(booksRepository).findById(1L);
        verify(booksRepository, never()).save(any());
    }

    @Test
    void testDeleteBook_Found() {
        when(booksRepository.existsById(1L)).thenReturn(true);

        boolean result = bookService.deleteBook(1L);

        assertTrue(result);
        verify(booksRepository).existsById(1L);
        verify(booksRepository).deleteById(1L);
    }

    @Test
    void testDeleteBook_NotFound() {
        when(booksRepository.existsById(1L)).thenReturn(false);

        boolean result = bookService.deleteBook(1L);

        assertFalse(result);
        verify(booksRepository).existsById(1L);
        verify(booksRepository, never()).deleteById(any());
    }

    @Test
    void testSearchBooks_ByTitle() {
        List<Books> books = Arrays.asList(testBook);
        when(booksRepository.findByTitleContainingIgnoreCase("Java")).thenReturn(books);
        when(bookMapper.toBookOutput(testBook)).thenReturn(testOutputDTO);

        List<OutputBookDTO> result = bookService.searchBooks("Java", null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(booksRepository).findByTitleContainingIgnoreCase("Java");
    }

    @Test
    void testSearchBooks_ByAuthor() {
        List<Books> books = Arrays.asList(testBook);
        when(booksRepository.findByAuthorContainingIgnoreCase("John")).thenReturn(books);
        when(bookMapper.toBookOutput(testBook)).thenReturn(testOutputDTO);

        List<OutputBookDTO> result = bookService.searchBooks(null, "John", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(booksRepository).findByAuthorContainingIgnoreCase("John");
    }
}
