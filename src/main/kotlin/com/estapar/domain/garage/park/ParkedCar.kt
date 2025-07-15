package com.estapar.domain.garage.park

import com.estapar.domain.garage.carEntry.CarEntry
import com.estapar.domain.garage.spot.Spot
import java.time.LocalDateTime
import java.time.LocalDateTime.now

data class ParkedCar(
    val id: Long? = null,
    val spot: Spot,
    val carEntry: CarEntry,
    val licensePlate: String,
    val parkingTime: LocalDateTime = now()) {

    fun isSpotStillOccupied() =
        spot.isStillOccupied()

    fun isOutsideOpeningHours() =
        spot.isSectorClosedFor(parkingTime = parkingTime.toLocalTime())

    fun hasOvercapacityAlert(): Boolean =
        spot.hasOvercapacityAlert()

    companion object {
        fun of(parkAttempt: ParkAttempt, carEntry: CarEntry, spot: Spot): ParkedCar =
            ParkedCar(
                spot = spot,
                carEntry = carEntry,
                licensePlate = parkAttempt.licensePlate)
    }

}
