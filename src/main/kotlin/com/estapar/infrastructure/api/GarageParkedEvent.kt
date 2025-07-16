package com.estapar.infrastructure.api

import com.estapar.domain.car.park.ParkAttempt
import com.fasterxml.jackson.annotation.JsonProperty

open class GarageParkedEvent(
    @JsonProperty("lat") val latitude: Double,
    @JsonProperty("lng") val longitude: Double,
    @JsonProperty("license_plate") licensePlate: String,
    @JsonProperty("event_type") eventType: GarageEventType
) : GarageWebhookEvent(licensePlate, eventType) {

    fun toDomain(): ParkAttempt =
        ParkAttempt(
            latitude = latitude,
            longitude = longitude,
            licensePlate = licensePlate)

}
