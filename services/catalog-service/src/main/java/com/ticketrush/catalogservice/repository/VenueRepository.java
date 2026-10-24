package com.ticketrush.catalogservice.repository;

import com.ticketrush.catalogservice.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VenueRepository extends JpaRepository<Venue, UUID> {
}
