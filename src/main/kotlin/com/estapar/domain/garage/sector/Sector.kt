package com.estapar.domain.garage.sector

import java.math.BigDecimal
import java.time.LocalTime

data class Sector(
    val id: Long? = null,
    val name: String,
    val basePrice: BigDecimal,
    val maxCapacity: Int,
    val openHour: LocalTime,
    val closeHour: LocalTime,
    val durationLimitMinutes: Int
)
