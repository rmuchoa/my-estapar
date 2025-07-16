package com.estapar.domain.car.logging

import com.estapar.domain.car.GarageLoggingStatus
import com.estapar.domain.car.entry.CarEntry
import reactor.core.publisher.Mono
import java.time.LocalDateTime

open class GarageLoggingService(
    val repository: GarageLoggingRepository
) {

    fun logEntry(carEntry: CarEntry): Mono<GarageLogging> =
        repository.saveGarageLogging(GarageLogging.of(carEntry))

    fun logExit(garageLogging: GarageLogging): Mono<GarageLogging> =
        repository.saveGarageLogging(garageLogging = garageLogging.copy(
            exitTime = LocalDateTime.now(),
            status = GarageLoggingStatus.CLOSED))

    fun findActiveGarageLoggingBy(licensePlate: String): Mono<GarageLogging> =
        repository.findActiveByLicensePlate(licensePlate)

}