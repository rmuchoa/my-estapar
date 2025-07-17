package com.estapar.infrastructure.api.dto

import com.estapar.domain.car.logging.GarageLogging
import com.estapar.domain.garage.spot.Spot
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.time.LocalTime

data class SpotReportResponse(
    @JsonProperty("ocupied") val ocupied: Boolean,
    @JsonProperty("entry_time") val entryTime: LocalDateTime,
    @JsonProperty("time_parked") val timeParked: LocalTime) {

    companion object {
        fun of(garageLogging: GarageLogging, spot: Spot): SpotReportResponse =
            SpotReportResponse(
                ocupied = spot.occupied,
                entryTime = garageLogging.entryTime,
                timeParked = garageLogging.getUntilNowDuration().inWholeSeconds
                    .let { secondOfDay -> LocalTime.ofSecondOfDay(secondOfDay) })
    }

}
