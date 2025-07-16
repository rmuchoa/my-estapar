package com.estapar.infrastructure.api

import com.estapar.domain.car.entry.CarEntry
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

open class GarageEntryEvent(
    @JsonProperty("entry_time") val entryTime: LocalDateTime,
    @JsonProperty("license_plate") licensePlate: String,
    @JsonProperty("event_type") eventType: GarageEventType
) : GarageWebhookEvent(licensePlate, eventType) {

    fun toDomain(): CarEntry =
        CarEntry(entryTime = entryTime, licensePlate = licensePlate)

}
