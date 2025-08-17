package org.newnop.repository;


import org.newnop.entity.Rentals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalsRepository extends JpaRepository<Rentals, Long> {
    List<Rentals> findByUserDetailsContainingIgnoreCase(String userDetails);
    List<Rentals> findByRentalDateBetween(LocalDate startDate, LocalDate endDate);
    List<Rentals> findByReturnDateIsNull();
}