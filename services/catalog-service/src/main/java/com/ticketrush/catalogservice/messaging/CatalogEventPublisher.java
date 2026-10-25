package com.ticketrush.catalogservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketrush.catalogservice.entity.*;
import com.ticketrush.catalogservice.repository.OutboxEventRepository;
import com.ticketrush.commonevents.envelope.EventEnvelope;
import com.ticketrush.commonevents.payload.catalog.*;
import com.ticketrush.commonweb.filter.CorrelationIdFilter;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class CatalogEventPublisher {

    private static final String AGGREGATE_TYPE = "EVENT";

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public CatalogEventPublisher(OutboxEventRepository outboxEventRepository, ObjectMapper objectMapper) {
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void publishEventPublished(Event event, Venue venue, List<PriceTier> priceTiers) {
        EventPublished payload = new EventPublished(
                event.getId(),
                event.getOrganizerId(),
                event.getTitle(),
                event.getDescription(),
                event.getCategory(),
                venue.getId(),
                venue.getName(),
                venue.getCity(),
                event.getStartsAt(),
                event.getEndsAt(),
                minPrice(priceTiers),
                maxPrice(priceTiers),
                event.getVersion());
        save(event.getId(), "EventPublished", payload);
    }

    @Transactional
    public void publishEventUpdated(Event event, Venue venue, List<PriceTier> priceTiers) {
        EventUpdated payload = new EventUpdated(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getCategory(),
                venue.getId(),
                venue.getName(),
                venue.getCity(),
                event.getStartsAt(),
                event.getEndsAt(),
                minPrice(priceTiers),
                maxPrice(priceTiers),
                event.getVersion());
        save(event.getId(), "EventUpdated", payload);
    }

    @Transactional
    public void publishEventCancelled(Event event, String reason) {
        EventCancelled payload = new EventCancelled(
                event.getId(),
                reason,
                Instant.now(),
                event.getVersion());

        save(event.getId(), "EventCancelled", payload);
    }

    private <T> void save(UUID aggregateId, String eventType, T payload) {
        EventEnvelope<T> envelope = new EventEnvelope<>(
                UUID.randomUUID(),
                eventType,
                AGGREGATE_TYPE,
                aggregateId,
                1L,
                Instant.now(),
                MDC.get(CorrelationIdFilter.MDC_KEY),
                payload);

        try {
            String json = objectMapper.writeValueAsString(envelope);
            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .aggregateType(AGGREGATE_TYPE)
                    .aggregateId(aggregateId)
                    .eventType(eventType)
                    .payload(json)
                    .build();
            outboxEventRepository.save(outboxEvent);

        } catch (Exception ex) {
            throw new IllegalStateException("Failed to serialize outbox payload for " + eventType, ex);
        }
    }

    private long minPrice(List<PriceTier> priceTiers) {
        return priceTiers.stream().mapToLong(PriceTier::getAmountMinor).min().orElse(0L);
    }

    private long maxPrice(List<PriceTier> priceTiers) {
        return priceTiers.stream().mapToLong(PriceTier::getAmountMinor).max().orElse(0L);
    }
}