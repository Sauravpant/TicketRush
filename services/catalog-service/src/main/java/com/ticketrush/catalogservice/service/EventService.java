package com.ticketrush.catalogservice.service;

import com.ticketrush.catalogservice.dto.request.EventCreateRequest;
import com.ticketrush.catalogservice.dto.request.EventUpdateRequest;
import com.ticketrush.catalogservice.dto.response.EventResponse;
import com.ticketrush.commonweb.response.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface EventService {

    EventResponse createEvent(UUID organizerId, EventCreateRequest request, MultipartFile coverImage);
    EventResponse updateEvent(UUID organizerId, UUID eventId, EventUpdateRequest request);
    EventResponse publishEvent(UUID organizerId, UUID eventId);
    EventResponse cancelEvent(UUID organizerId, UUID eventId, String reason);
    EventResponse getEvent(UUID eventId);
    PageResponse<EventResponse> listEvents(Pageable pageable);
}