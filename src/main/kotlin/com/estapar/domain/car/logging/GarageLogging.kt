package com.estapar.domain.car.logging

import com.estapar.domain.car.GarageLoggingStatus
import com.estapar.domain.car.entry.CarEntry
import java.time.Duration
import java.time.LocalDateTime
import kotlin.time.toKotlinDuration

data class GarageLogging(
    val id: Long? = null,
    val licensePlate: String,
    val entryTime: LocalDateTime,
    val exitTime: LocalDateTime? = null,
    val status: GarageLoggingStatus = GarageLoggingStatus.ACTIVE) {

    fun getBillingDuration(): kotlin.time.Duration =
        Duration.between(entryTime, exitTime)
            .let { duration -> duration.toKotlinDuration() }

    companion object {
        fun of(carEntry: CarEntry): GarageLogging =
            GarageLogging(
                licensePlate = carEntry.licensePlate,
                entryTime = carEntry.entryTime)
    }

}
