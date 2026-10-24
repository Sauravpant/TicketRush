package com.ticketrush.catalogservice.mapper;

import com.ticketrush.catalogservice.dto.response.EventResponse;
import com.ticketrush.catalogservice.dto.response.EventSummaryResponse;
import com.ticketrush.catalogservice.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventResponse toResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getOrganizerId(),
                event.getVenue().getId(),
                event.getTitle(),
                event.getShortDescription(),
                event.getDescription(),
                event.getCategory(),
                event.getCoverImagePublicId(),
                event.getStartsAt(),
                event.getEndsAt(),
                event.getSalesOpenAt(),
                event.getSalesCloseAt(),
                event.getStatus(),
                event.getVersion(),
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }

    public EventSummaryResponse toSummaryResponse(Event event) {
        return new EventSummaryResponse(
                event.getId(),
                event.getTitle(),
                event.getShortDescription(),
                event.getCategory(),
                event.getCoverImagePublicId(),
                event.getStartsAt()
        );
    }
}