package com.estapar.infrastructure.api

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

open class GarageExitEvent(
    @JsonProperty("exit_time") val exitTime: LocalDateTime,
    @JsonProperty("license_plate") licensePlate: String,
    @JsonProperty("event_type") eventType: GarageEventType
) : GarageWebhookEvent(licensePlate, eventType)
