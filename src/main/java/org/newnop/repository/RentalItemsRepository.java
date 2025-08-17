package org.newnop.repository;


import org.newnop.entity.RentalItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalItemsRepository extends JpaRepository<RentalItems, Long> {
    List<RentalItems> findByRentalRentalId(Long rentalId);
    List<RentalItems> findByBookBookId(Long bookId);
}