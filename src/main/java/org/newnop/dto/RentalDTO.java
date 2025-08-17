package org.newnop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Rental data transfer object")
public class RentalDTO {

    @Schema(description = "Unique identifier of the rental", example = "1")
    private Long rentalId;

    @Schema(description = "User details", example = "John Smith - john@email.com")
    private String userDetails;

    @Schema(description = "Rental date", example = "2024-01-01")
    private LocalDate rentalDate;

    @Schema(description = "Return date", example = "2024-01-15")
    private LocalDate returnDate;

    @Schema(description = "List of rented books")
    private List<BookDTO> books;
}