package com.ticketrush.commonevents;

public final class KafkaTopics {

    private KafkaTopics() {}

    public static final String CATALOG_EVENTS = "catalog.events";
    public static final String INVENTORY_EVENTS = "inventory.events";
    public static final String BOOKING_EVENTS = "booking.events";
    public static final String PAYMENT_EVENTS = "payment.events";
    public static final String TICKET_EVENTS = "ticket.events";

    public static final String CATALOG_EVENTS_RETRY = "catalog.events.retry";
    public static final String INVENTORY_EVENTS_RETRY = "inventory.events.retry";
    public static final String BOOKING_EVENTS_RETRY = "booking.events.retry";
    public static final String PAYMENT_EVENTS_RETRY = "payment.events.retry";
    public static final String TICKET_EVENTS_RETRY = "ticket.events.retry";

    public static final String CATALOG_EVENTS_DLT = "catalog.events.dlt";
    public static final String INVENTORY_EVENTS_DLT = "inventory.events.dlt";
    public static final String BOOKING_EVENTS_DLT = "booking.events.dlt";
    public static final String PAYMENT_EVENTS_DLT = "payment.events.dlt";
    public static final String TICKET_EVENTS_DLT = "ticket.events.dlt";
}