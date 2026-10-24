package com.ticketrush.catalogservice.repository;

import com.ticketrush.catalogservice.entity.PriceTier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PriceTierRepository extends JpaRepository<PriceTier, UUID> {

    List<PriceTier> findByEventId(UUID eventId);
}