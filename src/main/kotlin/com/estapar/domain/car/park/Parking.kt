package com.estapar.domain.car.park

import com.estapar.domain.car.park.attempt.ParkAttempt
import com.estapar.domain.garage.spot.Spot
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import kotlin.time.Duration

data class Parking(
    val id: Long? = null,
    val spot: Spot,
    val licensePlate: String,
    val parkingTime: LocalDateTime = now(),
    val unparkingTime: LocalDateTime? = null,
    val priceRule: DynamicPriceRule = spot.definePriceRuleByCapacity(),
    val status: ParkingStatus = ParkingStatus.ENTERED
) {

    fun isSpotStillOccupied() =
        spot.isStillOccupied()

    fun isOutsideOpeningHours() =
        spot.isSectorClosedFor(parkingTime = parkingTime.toLocalTime())

    fun hasReachedSectorMaxCapacity(): Boolean =
        spot.hasReachedSectorMaxCapacity()

    fun generateChargeFor(billingDuration: Duration): BigDecimal =
        spot.generateChargeFor(billingDuration, priceRule)

    companion object {
        fun of(parkAttempt: ParkAttempt, spot: Spot): Parking =
            Parking(
                spot = spot,
                licensePlate = parkAttempt.licensePlate)
    }

}
