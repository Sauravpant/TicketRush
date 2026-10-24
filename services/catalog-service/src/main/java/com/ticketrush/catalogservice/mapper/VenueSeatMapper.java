package com.ticketrush.catalogservice.mapper;

import com.ticketrush.catalogservice.dto.response.VenueSeatResponse;
import com.ticketrush.catalogservice.entity.VenueSeat;
import org.springframework.stereotype.Component;

@Component
public class VenueSeatMapper {

    public VenueSeatResponse toResponse(VenueSeat seat) {
        return new VenueSeatResponse(
                seat.getId(),
                seat.getSection().getId(),
                seat.getRowLabel(),
                seat.getSeatNumber()
        );
    }
}