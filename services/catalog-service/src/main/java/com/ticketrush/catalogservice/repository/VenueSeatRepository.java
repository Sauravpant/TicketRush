package com.ticketrush.catalogservice.repository;

import com.ticketrush.catalogservice.entity.VenueSeat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VenueSeatRepository extends JpaRepository<VenueSeat, UUID> {

    Page<VenueSeat> findBySectionId(UUID sectionId, Pageable pageable);
}
