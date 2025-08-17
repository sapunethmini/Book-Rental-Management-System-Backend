package org.newnop.service;
import org.newnop.dto.CreateRentalRequest;
import org.newnop.dto.RentalDTO;

import java.util.List;

public interface RentalServiceInterface {

    RentalDTO createRental(CreateRentalRequest request);

    List<RentalDTO> getAllRentals();
    RentalDTO getRentalById(Long id);

    RentalDTO updateRental(Long id, RentalDTO rentalDTO);

    RentalDTO returnBooks(Long rentalId);
}