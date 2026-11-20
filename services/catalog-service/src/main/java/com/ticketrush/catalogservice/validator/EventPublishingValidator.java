package com.ticketrush.catalogservice.validator;

import com.ticketrush.catalogservice.entity.Event;
import com.ticketrush.catalogservice.entity.PriceTier;
import com.ticketrush.catalogservice.enums.EventStatus;
import com.ticketrush.catalogservice.exception.EventNotPublishableException;
import com.ticketrush.catalogservice.exception.InvalidEventStateException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventPublishingValidator {

    public void validate(Event event, List<PriceTier> priceTiers) {
        if(event.getStatus() != EventStatus.DRAFT) {
            throw new InvalidEventStateException(event.getId(), event.getStatus());
        }

        if(!event.getEndsAt().isAfter(event.getStartsAt())) {
            throw new EventNotPublishableException(event.getId(), "endsAt must be after startsAt");
        }

        if (event.getSalesOpenAt() != null && event.getSalesCloseAt() != null
                && !event.getSalesCloseAt().isAfter(event.getSalesOpenAt())) {
            throw new EventNotPublishableException(event.getId(), "salesCloseAt must be after salesOpenAt");
        }

        if (priceTiers.isEmpty()) {
            throw new EventNotPublishableException(event.getId(), "event has no price tiers");
        }
    }
}
