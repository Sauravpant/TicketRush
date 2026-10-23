package com.ticketrush.catalogservice.entity;

import com.ticketrush.catalogservice.enums.EventStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "event",
    indexes = {
        @Index(name = "idx_event_venue_id", columnList = "venue_id"),
        @Index(name = "idx_event_organizer_id", columnList = "organizer_id"),
        @Index(name = "idx_event_status_starts_at", columnList = "status, starts_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    // should be set from JWT claims
    @Column(name = "organizer_id", nullable = false)
    private UUID organizerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @Column(nullable = false)
    private String title;

    @Column(name = "short_description", length = 500)
    private String shortDescription;

    @Lob
    private String description;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(name = "cover_image_public_id")
    private String coverImagePublicId;

    // private for deletion of asset from cloudinary
    @Column(name = "cover_image_asset_id")
    private String coverImageAssetId;

    @Column(name = "starts_at", nullable = false)
    private Instant startsAt;

    @Column(name = "ends_at", nullable = false)
    private Instant endsAt;

    @Column(name = "sales_open_at")
    private Instant salesOpenAt;

    @Column(name = "sales_close_at")
    private Instant salesCloseAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EventStatus status;

    @Version
    @Column(nullable = false)
    private Long version;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}