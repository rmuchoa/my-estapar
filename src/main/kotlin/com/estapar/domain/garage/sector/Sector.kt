package com.estapar.domain.garage.sector

import com.estapar.domain.garage.spot.Spot
import java.math.BigDecimal
import java.time.LocalTime
import kotlin.time.Duration

data class Sector(
    val id: Long? = null,
    val name: String,
    val basePrice: BigDecimal,
    val maxCapacity: Int,
    val openHour: LocalTime,
    val closeHour: LocalTime,
    val durationLimitMinutes: Int,
    val spots: List<Spot> = emptyList()) {

    fun isClosedFor(parkingTime: LocalTime): Boolean =
        parkingTime.isBefore(openHour) && parkingTime.isAfter(closeHour)

    fun hasOverDurationAlert(duration: Duration): Boolean =
        duration.inWholeMinutes > durationLimitMinutes

    fun hasOverCapacityAlert(): Boolean =
        spots.size > maxCapacity

}
