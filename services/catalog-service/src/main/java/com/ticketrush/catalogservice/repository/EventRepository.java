package com.ticketrush.catalogservice.repository;

import com.ticketrush.catalogservice.entity.Event;
import com.ticketrush.catalogservice.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface EventRepository  extends JpaRepository<Event, UUID> {

    List<Event> findByStatusOrderByStartsAtAsc(EventStatus status);
    List<Event> findByOrganizerId(UUID organizerId);
    List<Event> findByVenueId(UUID venueId);
}
