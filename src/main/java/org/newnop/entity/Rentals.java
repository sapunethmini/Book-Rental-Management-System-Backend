package org.newnop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "rentals")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rentals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    private String userDetails;
    private LocalDate rentalDate;
    private LocalDate returnDate;

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
    private List<RentalItems> rentalItems;
}