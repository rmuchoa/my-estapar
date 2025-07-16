package com.estapar.domain.car.logging

import com.estapar.domain.car.GarageLoggingStatus
import com.estapar.domain.car.entry.CarEntry
import java.time.LocalDateTime

data class GarageLogging(
    val id: Long? = null,
    val licensePlate: String,
    val entryTime: LocalDateTime,
    val exitTime: LocalDateTime? = null,
    val status: GarageLoggingStatus = GarageLoggingStatus.ACTIVE) {

    companion object {
        fun of(carEntry: CarEntry): GarageLogging =
            GarageLogging(
                licensePlate = carEntry.licensePlate,
                entryTime = carEntry.entryTime)
    }

}
