package org.newnop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.newnop.dto.CreateRentalRequest;
import org.newnop.dto.RentalDTO;
import org.newnop.service.RentalServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@CrossOrigin(origins = "*")
@Tag(name = "Rentals", description = "Rental management APIs")
public class RentalController {

    @Autowired
    private RentalServiceInterface rentalService;

    @Operation(summary = "Create new rental", description = "Create a new book rental")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rental created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid rental data")
    })
    @PostMapping
    public ResponseEntity<RentalDTO> createRental(@RequestBody CreateRentalRequest request) {
        try {
            RentalDTO createdRental = rentalService.createRental(request);
            return new ResponseEntity<>(createdRental, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get all rentals", description = "Retrieve all rental records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved rentals"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<RentalDTO>> getAllRentals() {
        try {
            List<RentalDTO> rentals = rentalService.getAllRentals();
            return new ResponseEntity<>(rentals, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get rental by ID", description = "Retrieve a specific rental by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental found"),
            @ApiResponse(responseCode = "404", description = "Rental not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRentalById(
            @Parameter(description = "Rental ID", required = true) @PathVariable Long id) {
        try {
            RentalDTO rental = rentalService.getRentalById(id);
            if (rental != null) {
                return new ResponseEntity<>(rental, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update rental", description = "Update an existing rental")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental updated successfully"),
            @ApiResponse(responseCode = "404", description = "Rental not found"),
            @ApiResponse(responseCode = "400", description = "Invalid rental data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RentalDTO> updateRental(
            @Parameter(description = "Rental ID", required = true) @PathVariable Long id,
            @RequestBody RentalDTO rentalDTO) {
        try {
            RentalDTO updatedRental = rentalService.updateRental(id, rentalDTO);
            if (updatedRental != null) {
                return new ResponseEntity<>(updatedRental, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Return books", description = "Mark books as returned and make them available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books returned successfully"),
            @ApiResponse(responseCode = "404", description = "Rental not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}/return")
    public ResponseEntity<RentalDTO> returnBooks(
            @Parameter(description = "Rental ID", required = true) @PathVariable Long id) {
        try {
            RentalDTO rental = rentalService.returnBooks(id);
            if (rental != null) {
                return new ResponseEntity<>(rental, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}