package com.estapar.domain.car.logging

import com.estapar.domain.car.entry.CarEntry
import reactor.core.publisher.Mono

open class GarageLoggingService(
    val repository: GarageLoggingRepository
) {

    fun logEntry(carEntry: CarEntry): Mono<GarageLogging> =
        repository.logGarageEntry(GarageLogging.of(carEntry))

    fun findActiveGarageLoggingBy(licensePlate: String): Mono<GarageLogging> =
        repository.findActiveByLicensePlate(licensePlate)
            .switchIfEmpty(Mono.error {
                NotFoundGarageLoggingException(
                    message = "No active car logging was found for license plate: '$licensePlate'"
                )
            })

}