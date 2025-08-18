package org.newnop.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.newnop.dto.rental.InputRentalDTO;
import org.newnop.dto.rental.OutputRentalDTO;
import org.newnop.entity.Books;
import org.newnop.entity.RentalItems;
import org.newnop.entity.Rentals;
import org.newnop.mapper.RentalMapper;
import org.newnop.repository.BooksRepository;
import org.newnop.repository.RentalItemsRepository;
import org.newnop.repository.RentalsRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceImplTests {

    @Mock
    private RentalsRepository rentalsRepository;

    @Mock
    private BooksRepository booksRepository;

    @Mock
    private RentalItemsRepository rentalItemsRepository;

    @Mock
    private RentalMapper rentalMapper;

    @InjectMocks
    private RentalServiceImpl rentalService;

    private Rentals testRental;
    private InputRentalDTO testInputDTO;
    private OutputRentalDTO testOutputDTO;
    private Books testBook1;
    private Books testBook2;
    private RentalItems testRentalItem;

    @BeforeEach
    void setUp() {
        testRental = new Rentals();
        testRental.setRentalId(1L);
        testRental.setUserDetails("John Smith - john@email.com");
        testRental.setRentalDate(LocalDate.of(2024, 1, 1));
        testRental.setReturnDate(LocalDate.of(2024, 1, 15));

        testInputDTO = new InputRentalDTO();
        testInputDTO.setUserDetails("John Smith - john@email.com");
        testInputDTO.setRentalDate(LocalDate.of(2024, 1, 1));
        testInputDTO.setReturnDate(LocalDate.of(2024, 1, 15));
        testInputDTO.setBookIds(Arrays.asList(1L, 2L));

        testOutputDTO = new OutputRentalDTO();
        testOutputDTO.setRentalId(1L);
        testOutputDTO.setUserDetails("John Smith - john@email.com");
        testOutputDTO.setRentalDate(LocalDate.of(2024, 1, 1));
        testOutputDTO.setReturnDate(LocalDate.of(2024, 1, 15));

        testBook1 = new Books();
        testBook1.setBookId(1L);
        testBook1.setTitle("Java Programming");
        testBook1.setAuthor("John Doe");
        testBook1.setGenre("Programming");
        testBook1.setAvailable(true);

        testBook2 = new Books();
        testBook2.setBookId(2L);
        testBook2.setTitle("Python Guide");
        testBook2.setAuthor("Jane Smith");
        testBook2.setGenre("Programming");
        testBook2.setAvailable(true);

        testRentalItem = new RentalItems();
        testRentalItem.setRental(testRental);
        testRentalItem.setBook(testBook1);
    }

    @Test
    void testCreateRental_Success() {
        when(rentalMapper.toRentalEntity(testInputDTO)).thenReturn(testRental);
        when(rentalsRepository.save(testRental)).thenReturn(testRental);
        when(booksRepository.findById(1L)).thenReturn(Optional.of(testBook1));
        when(booksRepository.findById(2L)).thenReturn(Optional.of(testBook2));
        
        // Mock the calls that getRentalById will make
        when(rentalsRepository.findById(1L)).thenReturn(Optional.of(testRental));
        when(rentalMapper.toRentalOutput(testRental)).thenReturn(testOutputDTO);

        OutputRentalDTO result = rentalService.createRental(testInputDTO);

        assertNotNull(result);
        verify(rentalMapper).toRentalEntity(testInputDTO);
        verify(rentalsRepository).save(testRental);
        verify(booksRepository, times(2)).findById(any());
        verify(rentalItemsRepository, times(2)).save(any(RentalItems.class));
        verify(booksRepository, times(2)).save(any(Books.class));
    }

    @Test
    void testCreateRental_EmptyBookIds() {
        InputRentalDTO emptyDTO = new InputRentalDTO();
        emptyDTO.setUserDetails("John Smith - john@email.com");
        emptyDTO.setRentalDate(LocalDate.of(2024, 1, 1));
        emptyDTO.setReturnDate(LocalDate.of(2024, 1, 15));
        emptyDTO.setBookIds(Arrays.asList());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> rentalService.createRental(emptyDTO)
        );

        assertEquals("At least one book must be selected for rental", exception.getMessage());
    }

    @Test
    void testCreateRental_BookNotFound() {
        when(rentalMapper.toRentalEntity(testInputDTO)).thenReturn(testRental);
        when(rentalsRepository.save(testRental)).thenReturn(testRental);
        when(booksRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> rentalService.createRental(testInputDTO)
        );

        assertEquals("Book with ID 1 not found", exception.getMessage());
    }

    @Test
    void testCreateRental_BookNotAvailable() {
        testBook1.setAvailable(false);
        when(rentalMapper.toRentalEntity(testInputDTO)).thenReturn(testRental);
        when(rentalsRepository.save(testRental)).thenReturn(testRental);
        when(booksRepository.findById(1L)).thenReturn(Optional.of(testBook1));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> rentalService.createRental(testInputDTO)
        );

        assertEquals("Book with ID 1 is not available for rental", exception.getMessage());
    }

    @Test
    void testGetAllRentals() {
        List<Rentals> rentals = Arrays.asList(testRental);
        when(rentalsRepository.findAll()).thenReturn(rentals);
        when(rentalMapper.toRentalOutput(testRental)).thenReturn(testOutputDTO);

        List<OutputRentalDTO> result = rentalService.getAllRentals();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(rentalsRepository).findAll();
        verify(rentalMapper).toRentalOutput(testRental);
    }

    @Test
    void testGetRentalById_Found() {
        when(rentalsRepository.findById(1L)).thenReturn(Optional.of(testRental));
        when(rentalMapper.toRentalOutput(testRental)).thenReturn(testOutputDTO);

        OutputRentalDTO result = rentalService.getRentalById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getRentalId());
        verify(rentalsRepository).findById(1L);
        verify(rentalMapper).toRentalOutput(testRental);
    }

    @Test
    void testGetRentalById_NotFound() {
        when(rentalsRepository.findById(1L)).thenReturn(Optional.empty());

        OutputRentalDTO result = rentalService.getRentalById(1L);

        assertNull(result);
        verify(rentalsRepository).findById(1L);
        verify(rentalMapper, never()).toRentalOutput(any());
    }

    @Test
    void testUpdateRental_Found() {
        when(rentalsRepository.findById(1L)).thenReturn(Optional.of(testRental));
        when(rentalsRepository.save(testRental)).thenReturn(testRental);
        when(rentalMapper.toRentalOutput(testRental)).thenReturn(testOutputDTO);

        OutputRentalDTO result = rentalService.updateRental(1L, testInputDTO);

        assertNotNull(result);
        verify(rentalsRepository).findById(1L);
        verify(rentalMapper).updateRentalFromInput(testRental, testInputDTO);
        verify(rentalsRepository).save(testRental);
        verify(rentalMapper).toRentalOutput(testRental);
    }

    @Test
    void testUpdateRental_NotFound() {
        when(rentalsRepository.findById(1L)).thenReturn(Optional.empty());

        OutputRentalDTO result = rentalService.updateRental(1L, testInputDTO);

        assertNull(result);
        verify(rentalsRepository).findById(1L);
        verify(rentalsRepository, never()).save(any());
    }

    @Test
    void testReturnBooks_Success() {
        List<RentalItems> rentalItems = Arrays.asList(testRentalItem);
        
        when(rentalsRepository.findById(1L)).thenReturn(Optional.of(testRental));
        when(rentalItemsRepository.findByRentalRentalId(1L)).thenReturn(rentalItems);
        when(rentalsRepository.save(testRental)).thenReturn(testRental);
        when(rentalMapper.toRentalOutput(testRental)).thenReturn(testOutputDTO);

        OutputRentalDTO result = rentalService.returnBooks(1L);

        assertNotNull(result);
        verify(rentalsRepository).findById(1L);
        verify(rentalItemsRepository).findByRentalRentalId(1L);
        verify(booksRepository).save(testBook1);
        verify(rentalsRepository).save(testRental);
        assertTrue(testBook1.getAvailable()); // Book should be marked as available
    }

    @Test
    void testReturnBooks_NotFound() {
        when(rentalsRepository.findById(1L)).thenReturn(Optional.empty());

        OutputRentalDTO result = rentalService.returnBooks(1L);

        assertNull(result);
        verify(rentalsRepository).findById(1L);
        verify(rentalItemsRepository, never()).findByRentalRentalId(any());
    }
}
