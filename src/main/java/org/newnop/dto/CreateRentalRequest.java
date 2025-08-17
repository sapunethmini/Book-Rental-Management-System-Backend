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
@Schema(description = "Create rental request")
public class CreateRentalRequest {

    @Schema(description = "User details", example = "John Smith - john@email.com", required = true)
    private String userDetails;

    @Schema(description = "Rental date", example = "2024-01-01", required = true)
    private LocalDate rentalDate;

    @Schema(description = "Return date", example = "2024-01-15", required = true)
    private LocalDate returnDate;

    @Schema(description = "List of book IDs to rent", example = "[1, 2, 3]", required = true)
    private List<Long> bookIds;
}