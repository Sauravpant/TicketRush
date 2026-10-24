package com.ticketrush.catalogservice.repository;

import com.ticketrush.catalogservice.entity.VenueSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VenueSectionRepository  extends JpaRepository<VenueSection, UUID> {

    List<VenueSection> findByVenueId(UUID venueId);
}
