package com.estapar.infrastructure.client.dto

import com.estapar.domain.garage.sector.Sector
import com.estapar.domain.garage.spot.Spot
import com.fasterxml.jackson.annotation.JsonProperty

data class SpotDTO(
    @JsonProperty("id") val id: Long,
    @JsonProperty("sector") val sector: String,
    @JsonProperty("lat") val latitude: Double,
    @JsonProperty("lng") val longitude: Double,
    @JsonProperty("occupied") val occupied: Boolean
) {
    fun toDomain(sectorFillOperation: (String) -> Sector?): Spot {
        return Spot(
            sector = sectorFillOperation(sector),
            latitude = latitude,
            longitude = longitude,
            occupied = occupied)
    }
}