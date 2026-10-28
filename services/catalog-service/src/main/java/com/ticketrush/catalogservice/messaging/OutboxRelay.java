package com.ticketrush.catalogservice.messaging;

import com.ticketrush.catalogservice.entity.OutboxEvent;
import com.ticketrush.catalogservice.repository.OutboxEventRepository;
import com.ticketrush.commonevents.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;

@Slf4j
@Component
public class OutboxRelay {

    private static final String TOPIC = KafkaTopics.CATALOG_EVENTS;
    private static final int BATCH_SIZE = 100;

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxRelay(OutboxEventRepository outboxEventRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxEventRepository = outboxEventRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 5000)
    public void relay() {
        List<OutboxEvent> unpublished = outboxEventRepository
                .findByPublishedAtIsNullOrderByCreatedAtAsc(PageRequest.of(0, BATCH_SIZE));
        for (OutboxEvent event : unpublished) {
            publish(event);
        }
    }

    private void publish(OutboxEvent event) {
        SendResult<String, String> result;
        try {
            result = kafkaTemplate
                    .send(TOPIC, event.getAggregateId().toString(), event.getPayload())
                    .get();
        } catch (Exception ex) {
            log.error("Kafka send FAILED for outbox event {} (aggregate {}). It will retry on next poll",
                    event.getId(), event.getAggregateId(), ex);
            return;
        }

        log.info("Published {} for aggregate {} to partition {} offset {}",
                event.getEventType(), event.getAggregateId(),
                result.getRecordMetadata().partition(), result.getRecordMetadata().offset());

        try {
            markPublished(event);
        } catch (Exception ex) {
            log.warn("Kafka send SUCCEEDED but marking outbox event {} (aggregate {}) as published FAILED — "
                            + "next poll will re-send this message",
                    event.getId(), event.getAggregateId(), ex);
        }
    }

    @Transactional
    protected void markPublished(OutboxEvent event) {
        event.setPublishedAt(Instant.now());
        outboxEventRepository.save(event);
    }
}