package com.estapar.domain.car.park.report

import com.estapar.domain.car.logging.GarageLogging
import com.estapar.domain.car.park.Parking
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.time.Duration

data class ParkingReport(
    val licensePlate: String,
    val entryTime: LocalDateTime,
    val priceUntilNow: BigDecimal,
    val timeParked: LocalTime) {

    companion object {
        fun of(garageLogging: GarageLogging, parking: Parking): ParkingReport =
            garageLogging.getUntilNowDuration()
                .let { untilNowDuration -> of(garageLogging, parking, untilNowDuration) }

        fun of(garageLogging: GarageLogging, parking: Parking, untilNowDuration: Duration): ParkingReport =
            ParkingReport(
                licensePlate = garageLogging.licensePlate,
                entryTime = garageLogging.entryTime,
                priceUntilNow = parking.generateChargeFor(billingDuration = untilNowDuration),
                timeParked = untilNowDuration.inWholeSeconds
                    .let { secondOfDay -> LocalTime.ofSecondOfDay(secondOfDay) })

    }

}
