package com.ticketrush.catalogservice.dto.request;

import jakarta.validation.constraints.Size;

public record VenueUpdateRequest(

    @Size(max = 255, message = "Venue name must not exceed 255 characters")
    String name,

    @Size(max = 100, message = "City must not exceed 100 characters")
    String city,

    @Size(max = 500, message = "Address must not exceed 500 characters")
    String address,

    Double latitude,

    Double longitude,

    @Size(max = 50, message = "Timezone must not exceed 50 characters")
    String timezone
) {}
