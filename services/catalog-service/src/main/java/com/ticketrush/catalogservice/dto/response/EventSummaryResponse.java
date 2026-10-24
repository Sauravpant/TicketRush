package com.ticketrush.catalogservice.dto.response;

import java.time.Instant;
import java.util.UUID;

public record EventSummaryResponse(
    UUID id,
    String title,
    String shortDescription,
    String category,
    String coverImagePublicId,
    Instant startsAt
) {}
