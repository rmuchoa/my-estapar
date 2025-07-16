package com.estapar.infrastructure.client

import com.estapar.domain.garage.Garage
import com.fasterxml.jackson.annotation.JsonProperty

data class LoadGarageResponse(
    @JsonProperty("garage") val garage: List<GarageSectorDTO>,
    @JsonProperty("spots") val spots: List<SpotDTO>
) {

    fun toDomain(): Garage {
        return garage.map { it.toDomain() }
            .let { sectors ->
                Garage(
                    sectors = sectors,
                    spots = spots.map { dto ->
                        dto.toDomain { name: String ->
                            sectors.find { sector ->
                                sector.name.equals(other = name, ignoreCase = true)
                            }
                        }
                    })
            }
    }

}