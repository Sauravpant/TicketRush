package com.ticketrush.catalogservice.dto.response;

import java.util.UUID;

public record PriceTierResponse(
    UUID id,
    UUID eventId,
    UUID sectionId,
    String name,
    Long amountMinor,
    String currency
) {}
