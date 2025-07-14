package com.estapar.infrastructure.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "event_type",
    visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(value = GarageEntryEvent::class, name = "ENTRY"),
    JsonSubTypes.Type(value = GarageParkedEvent::class, name = "PARKED"),
    JsonSubTypes.Type(value = GarageExitEvent::class, name = "EXIT")
)
abstract class GarageWebhookEvent(
    val licensePlate: String,
    val eventType: GarageEventType
)
