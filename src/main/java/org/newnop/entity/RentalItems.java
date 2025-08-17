package org.newnop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rental_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalItemId;

    @ManyToOne
    @JoinColumn(name = "rental_id")
    private Rentals rental;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Books book;
}

