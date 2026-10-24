package com.ticketrush.catalogservice.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record EventUpdateRequest(

    // venueId intentionally excluded

    @Size(max = 255, message = "Title must not exceed 255 characters")
    String title,

    @Size(max = 500, message = "Short description must not exceed 500 characters")
    String shortDescription,

    String description,

    @Size(max = 50, message = "Category must not exceed 50 characters")
    String category,

    @Future(message = "Start time must be in the future")
    Instant startsAt,

    @Future(message = "End time must be in the future")
    Instant endsAt,

    Instant salesOpenAt,

    Instant salesCloseAt
) {}
