package org.newnop.service.impl;
import org.newnop.dto.BookDTO;
import org.newnop.dto.CreateRentalRequest;
import org.newnop.dto.RentalDTO;
import org.newnop.entity.Books;
import org.newnop.entity.RentalItems;
import org.newnop.entity.Rentals;
import org.newnop.repository.BooksRepository;
import org.newnop.repository.RentalItemsRepository;
import org.newnop.repository.RentalsRepository;
import org.newnop.service.RentalServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentalServiceImpl implements RentalServiceInterface {

    @Autowired
    private RentalsRepository rentalsRepository;

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private RentalItemsRepository rentalItemsRepository;

    @Override
    @Transactional
    public RentalDTO createRental(CreateRentalRequest request) {
        // Validate request
        if (request.getBookIds() == null || request.getBookIds().isEmpty()) {
            throw new IllegalArgumentException("At least one book must be selected for rental");
        }

        // Create rental
        Rentals rental = new Rentals();
        rental.setUserDetails(request.getUserDetails());
        rental.setRentalDate(request.getRentalDate());
        rental.setReturnDate(request.getReturnDate());

        Rentals savedRental = rentalsRepository.save(rental);

        // Create rental items for each book
        for (Long bookId : request.getBookIds()) {
            Optional<Books> bookOpt = booksRepository.findById(bookId);
            if (bookOpt.isPresent()) {
                Books book = bookOpt.get();
                if (book.getAvailable()) {
                    // Create rental item
                    RentalItems rentalItem = new RentalItems();
                    rentalItem.setRental(savedRental);
                    rentalItem.setBook(book);
                    rentalItemsRepository.save(rentalItem);

                    // Mark book as unavailable
                    book.setAvailable(false);
                    booksRepository.save(book);
                } else {
                    throw new IllegalStateException("Book with ID " + bookId + " is not available for rental");
                }
            } else {
                throw new IllegalArgumentException("Book with ID " + bookId + " not found");
            }
        }

        return getRentalById(savedRental.getRentalId());
    }

    @Override
    public List<RentalDTO> getAllRentals() {
        List<Rentals> rentals = rentalsRepository.findAll();
        return rentals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public RentalDTO getRentalById(Long id) {
        Optional<Rentals> rental = rentalsRepository.findById(id);
        return rental.map(this::convertToDTO).orElse(null);
    }

    @Override
    public RentalDTO updateRental(Long id, RentalDTO rentalDTO) {
        Optional<Rentals> optionalRental = rentalsRepository.findById(id);
        if (optionalRental.isPresent()) {
            Rentals rental = optionalRental.get();

            if (rentalDTO.getUserDetails() != null) {
                rental.setUserDetails(rentalDTO.getUserDetails());
            }
            if (rentalDTO.getRentalDate() != null) {
                rental.setRentalDate(rentalDTO.getRentalDate());
            }
            if (rentalDTO.getReturnDate() != null) {
                rental.setReturnDate(rentalDTO.getReturnDate());
            }

            Rentals updatedRental = rentalsRepository.save(rental);
            return convertToDTO(updatedRental);
        }
        return null;
    }

    @Override
    @Transactional
    public RentalDTO returnBooks(Long rentalId) {
        Optional<Rentals> rentalOpt = rentalsRepository.findById(rentalId);
        if (rentalOpt.isPresent()) {
            List<RentalItems> rentalItems = rentalItemsRepository.findByRentalRentalId(rentalId);

            // Mark all books as available
            for (RentalItems item : rentalItems) {
                Books book = item.getBook();
                book.setAvailable(true);
                booksRepository.save(book);
            }

            // Update return date
            Rentals rental = rentalOpt.get();
            rental.setReturnDate(java.time.LocalDate.now());
            rentalsRepository.save(rental);

            return convertToDTO(rental);
        }
        return null;
    }

    // Private helper method
    private RentalDTO convertToDTO(Rentals rental) {
        RentalDTO dto = new RentalDTO();
        dto.setRentalId(rental.getRentalId());
        dto.setUserDetails(rental.getUserDetails());
        dto.setRentalDate(rental.getRentalDate());
        dto.setReturnDate(rental.getReturnDate());

        // Get books for this rental
        List<RentalItems> rentalItems = rentalItemsRepository.findByRentalRentalId(rental.getRentalId());
        List<BookDTO> books = rentalItems.stream()
                .map(item -> new BookDTO(
                        item.getBook().getBookId(),
                        item.getBook().getTitle(),
                        item.getBook().getAuthor(),
                        item.getBook().getGenre(),
                        item.getBook().getAvailable()
                ))
                .collect(Collectors.toList());

        dto.setBooks(books);
        return dto;
    }
}