package com.estapar.infrastructure.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime

data class ParkingReportResponse(
    @JsonProperty("license_plate") val licensePlate: String,
    @JsonProperty("price_until_now") val priceUntilNow: BigDecimal,
    @JsonProperty("entry_time") val entryTime: LocalDateTime,
    @JsonProperty("time_parked") val timeParked: LocalTime)
