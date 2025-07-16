package com.estapar.domain.car.park

import com.estapar.domain.garage.spot.Spot
import java.time.LocalDateTime
import java.time.LocalDateTime.now

data class Parking(
    val id: Long? = null,
    val spot: Spot,
    val licensePlate: String,
    val parkingTime: LocalDateTime = now(),
    val unparkingTime: LocalDateTime? = null,
) {

    var priceRule: DynamicPriceRule = spot.definePriceRuleByCapacity()

    fun isSpotStillOccupied() =
        spot.isStillOccupied()

    fun isOutsideOpeningHours() =
        spot.isSectorClosedFor(parkingTime = parkingTime.toLocalTime())

    fun hasReachedSectorMaxCapacity(): Boolean =
        spot.hasReachedSectorMaxCapacity()

    companion object {
        fun of(parkAttempt: ParkAttempt, spot: Spot): Parking =
            Parking(
                spot = spot,
                licensePlate = parkAttempt.licensePlate)
    }

}
