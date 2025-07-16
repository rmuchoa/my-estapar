package com.estapar.domain.car.entry

import com.estapar.domain.car.logging.GarageLogging
import java.time.LocalDateTime

data class CarEntry(
    val licensePlate: String,
    val entryTime: LocalDateTime) {

    companion object {
        fun of(logging: GarageLogging): CarEntry =
            CarEntry(
                licensePlate = logging.licensePlate,
                entryTime = logging.entryTime)
    }

}
