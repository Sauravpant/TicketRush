package com.ticketrush.catalogservice.mapper;

import com.ticketrush.catalogservice.dto.response.VenueSectionResponse;
import com.ticketrush.catalogservice.entity.VenueSection;
import org.springframework.stereotype.Component;

@Component
public class VenueSectionMapper {

    public VenueSectionResponse toResponse(VenueSection section) {
        return new VenueSectionResponse(
                section.getId(),
                section.getVenue().getId(),
                section.getName(),
                section.getType(),
                section.getCapacity()
        );
    }
}