package com.estapar.domain.car.logging

import reactor.core.publisher.Mono

open class GarageLoggingService(
    val repository: GarageLoggingRepository
) {

    fun logGarageEntry(garageLogging: GarageLogging): Mono<GarageLogging> =
        repository.findActiveByLicensePlate(licensePlate = garageLogging.licensePlate)
            .flatMap { logging ->
                Mono.error<GarageLogging>(
                    AlreadyHasActiveGarageLoggingException(
                        message = "License plate '${garageLogging.licensePlate}' has already logged in garage recently!"))
            }
            .switchIfEmpty(repository.logEntry(garageLogging))

    fun findGarageLoggingBy(licensePlate: String): Mono<GarageLogging> =
        repository.findActiveByLicensePlate(licensePlate)
            .switchIfEmpty(Mono.error {
                NotFoundGarageLoggingException(
                    message = "No active car logging was found for license plate: '$licensePlate'"
                )
            })

}