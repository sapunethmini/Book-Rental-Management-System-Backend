package org.newnop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Book data transfer object")
public class BookDTO {

    @Schema(description = "Unique identifier of the book", example = "1")
    private Long bookId;

    @Schema(description = "Title of the book", example = "Java Programming", required = true)
    private String title;

    @Schema(description = "Author of the book", example = "John Doe", required = true)
    private String author;

    @Schema(description = "Genre of the book", example = "Programming")
    private String genre;

    @Schema(description = "Availability status of the book", example = "true")
    private Boolean available;
}