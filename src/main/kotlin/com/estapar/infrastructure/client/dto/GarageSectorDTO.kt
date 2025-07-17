package com.estapar.infrastructure.client.dto

import com.estapar.domain.garage.sector.Sector
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalTime

data class GarageSectorDTO(
    @JsonProperty("sector") val sector: String,
    @JsonProperty("base_price") val basePrice: BigDecimal,
    @JsonProperty("max_capacity") val maxCapacity: Int,
    @JsonProperty("open_hour") val openHour: LocalTime,
    @JsonProperty("close_hour") val closeHour: LocalTime,
    @JsonProperty("duration_limit_minutes") val durationLimitMinutes: Int
) {
    fun toDomain(): Sector {
        return Sector(
            name = sector,
            basePrice = basePrice,
            maxCapacity =  maxCapacity,
            openHour = openHour,
            closeHour = closeHour,
            durationLimitMinutes = durationLimitMinutes)
    }
}