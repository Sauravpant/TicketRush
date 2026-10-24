package com.ticketrush.catalogservice.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record EventCreateRequest(


    @NotNull(message = "Venue ID is required")
    UUID venueId,

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    String title,

    @Size(max = 500, message = "Short description must not exceed 500 characters")
    String shortDescription,

    String description,

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category must not exceed 50 characters")
    String category,

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    Instant startsAt,

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    Instant endsAt,

    Instant salesOpenAt,

    Instant salesCloseAt
) {}
