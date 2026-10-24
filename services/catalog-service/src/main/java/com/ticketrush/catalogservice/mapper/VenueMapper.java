package com.ticketrush.catalogservice.mapper;

import com.ticketrush.catalogservice.dto.response.VenueResponse;
import com.ticketrush.catalogservice.entity.Venue;
import org.springframework.stereotype.Component;

@Component
public class VenueMapper {

    public VenueResponse toResponse(Venue venue) {
        return new VenueResponse(
                venue.getId(),
                venue.getName(),
                venue.getCity(),
                venue.getAddress(),
                venue.getLatitude(),
                venue.getLongitude(),
                venue.getTimezone(),
                venue.getCreatedAt(),
                venue.getUpdatedAt()
        );
    }
}