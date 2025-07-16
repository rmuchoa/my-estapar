package com.estapar.domain.park

import com.estapar.domain.carEntry.CarEntry
import com.estapar.domain.garage.spot.Spot
import java.time.LocalDateTime
import java.time.LocalDateTime.now

data class ParkedCar(
    val id: Long? = null,
    val spot: Spot,
    val carEntry: CarEntry,
    val licensePlate: String,
    val parkingTime: LocalDateTime = now()
) {

    var priceRule: DynamicPriceRule = spot.definePriceRuleByCapacity()

    fun isSpotStillOccupied() =
        spot.isStillOccupied()

    fun isOutsideOpeningHours() =
        spot.isSectorClosedFor(parkingTime = parkingTime.toLocalTime())

    fun hasOvercapacityAlert(): Boolean =
        spot.hasReachedSectorMaxCapacity()

    companion object {
        fun of(parkAttempt: ParkAttempt, carEntry: CarEntry, spot: Spot): ParkedCar =
            ParkedCar(
                spot = spot,
                carEntry = carEntry,
                licensePlate = parkAttempt.licensePlate)
    }

}
