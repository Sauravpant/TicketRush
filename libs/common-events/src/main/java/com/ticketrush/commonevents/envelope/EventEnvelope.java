package com.ticketrush.commonevents.envelope;

import java.time.Instant;
import java.util.UUID;

public record EventEnvelope<T>(
    UUID eventId,
    String eventType,
    String aggregateType,
    UUID aggregateId,
    long version,
    Instant occurredAt,
    String correlationId,
    T payload) {

  public EventEnvelope {
    if (occurredAt == null) {
      occurredAt = Instant.now();
    }
  }
}
