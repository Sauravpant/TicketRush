package com.ticketrush.catalogservice.repository;

import com.ticketrush.catalogservice.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent> findByPublishedAtIsNullOrderByCreatedAtAsc();
}