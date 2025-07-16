package com.estapar.domain.car.logging

import com.estapar.domain.car.GarageLoggingStatus
import com.estapar.domain.car.entry.CarEntry
import com.estapar.domain.car.exit.CarExit
import reactor.core.publisher.Mono

open class GarageLoggingService(
    val repository: GarageLoggingRepository
) {

    fun logEntry(carEntry: CarEntry): Mono<GarageLogging> =
        repository.saveGarageLogging(GarageLogging.of(carEntry))

    fun logExit(garageLogging: GarageLogging, carExit: CarExit): Mono<GarageLogging> =
        repository.saveGarageLogging(garageLogging = garageLogging.copy(
            exitTime = carExit.exitTime,
            status = GarageLoggingStatus.CLOSED))

    fun findActiveGarageLoggingBy(licensePlate: String): Mono<GarageLogging> =
        repository.findActiveByLicensePlate(licensePlate)

}