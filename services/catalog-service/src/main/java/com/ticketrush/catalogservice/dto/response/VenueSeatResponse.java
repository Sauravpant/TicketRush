package com.ticketrush.catalogservice.dto.response;

import java.util.UUID;

public record VenueSeatResponse(
    UUID id,
    UUID sectionId,
    String rowLabel,
    Integer seatNumber
) {}
