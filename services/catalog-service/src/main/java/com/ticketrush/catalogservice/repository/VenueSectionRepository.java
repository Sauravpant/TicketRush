package com.ticketrush.catalogservice.repository;

import com.ticketrush.catalogservice.entity.VenueSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface VenueSectionRepository  extends JpaRepository<VenueSection, UUID> {

    Page<VenueSection> findByVenueId(UUID venueId, Pageable pageable);
}
