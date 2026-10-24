package com.ticketrush.catalogservice.repository;

import com.ticketrush.catalogservice.entity.VenueSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VenueSeatRepository extends JpaRepository<VenueSeat, UUID> {

    List<VenueSeat> findBySectionId(UUID sectionId);
}
