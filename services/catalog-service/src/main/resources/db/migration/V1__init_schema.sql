
CREATE TABLE venue (
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    city        VARCHAR(50) NOT NULL,
    address     VARCHAR(100),
    latitude    DOUBLE PRECISION,
    longitude   DOUBLE PRECISION,
    timezone    VARCHAR(50) NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL,
    updated_at  TIMESTAMPTZ NOT NULL
);

CREATE TABLE venue_section (
    id          UUID PRIMARY KEY,
    venue_id    UUID NOT NULL REFERENCES venue (id),
    name        VARCHAR(50) NOT NULL,
    type        VARCHAR(20) NOT NULL,
    capacity    INTEGER NOT NULL CHECK (capacity > 0)
);

CREATE INDEX idx_venue_section_venue_id ON venue_section (venue_id);


CREATE TABLE venue_seat (
    id              UUID PRIMARY KEY,
    section_id      UUID NOT NULL REFERENCES venue_section (id),
    row_label       VARCHAR(10),
    seat_number     INTEGER NOT NULL,
    CONSTRAINT uk_venue_seat_section_row_number UNIQUE (section_id, row_label, seat_number)
);


CREATE TABLE event (
    id                      UUID PRIMARY KEY,
    organizer_id            UUID NOT NULL,
    venue_id                UUID NOT NULL REFERENCES venue (id),
    title                   VARCHAR(255) NOT NULL,
    short_description       VARCHAR(500),
    description             TEXT,
    category                VARCHAR(50) NOT NULL,
    cover_image_public_id   VARCHAR(255),
    cover_image_asset_id    VARCHAR(255),
    starts_at               TIMESTAMPTZ NOT NULL,
    ends_at                 TIMESTAMPTZ NOT NULL,
    sales_open_at           TIMESTAMPTZ,
    sales_close_at          TIMESTAMPTZ,
    status                  VARCHAR(20) NOT NULL,
    version                 BIGINT NOT NULL DEFAULT 0,
    created_at              TIMESTAMPTZ NOT NULL,
    updated_at              TIMESTAMPTZ NOT NULL,
    CONSTRAINT chk_event_ends_after_starts CHECK (ends_at > starts_at),
    CONSTRAINT chk_event_sales_close_after_open CHECK (sales_close_at IS NULL OR sales_open_at IS NULL OR sales_close_at > sales_open_at)
);

CREATE INDEX idx_event_venue_id ON event (venue_id);
CREATE INDEX idx_event_organizer_id ON event (organizer_id);
CREATE INDEX idx_event_status_starts_at ON event (status, starts_at);


CREATE TABLE price_tier (
    id              UUID PRIMARY KEY,
    event_id        UUID NOT NULL REFERENCES event (id),
    section_id      UUID NOT NULL REFERENCES venue_section (id),
    name            VARCHAR(100) NOT NULL,
    amount_minor    BIGINT NOT NULL CHECK (amount_minor >= 0),
    currency        VARCHAR(3) NOT NULL,
    CONSTRAINT uk_price_tier_event_section UNIQUE (event_id, section_id)
);


CREATE TABLE outbox_event (
    id              UUID PRIMARY KEY,
    aggregate_type  VARCHAR(50) NOT NULL,
    aggregate_id    UUID NOT NULL,
    event_type      VARCHAR(100) NOT NULL,
    payload         JSONB NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL,
    published_at    TIMESTAMPTZ
);

CREATE INDEX idx_outbox_event_unpublished ON outbox_event (created_at) WHERE published_at IS NULL;