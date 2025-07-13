package com.estapar.domain.garage.spot

import com.estapar.domain.garage.sector.Sector

data class Spot(
    val id: Long? = null,
    val sector: Sector?,
    val latitude: Double,
    val longitude: Double,
    val occupied: Boolean
)