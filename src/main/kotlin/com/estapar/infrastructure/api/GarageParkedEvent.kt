package com.estapar.infrastructure.api

import com.fasterxml.jackson.annotation.JsonProperty

open class GarageParkedEvent(
    val lat: Double,
    val lng: Double,
    @JsonProperty("license_plate") licensePlate: String,
    @JsonProperty("event_type") eventType: GarageEventType
) : GarageWebhookEvent(licensePlate, eventType)
