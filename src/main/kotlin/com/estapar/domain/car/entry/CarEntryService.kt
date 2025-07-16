package com.estapar.domain.car.entry

import com.estapar.domain.car.logging.AlreadyHasActiveGarageLoggingException
import com.estapar.domain.car.logging.GarageLoggingService
import reactor.core.publisher.Mono

open class CarEntryService(
    val garageLoggingService: GarageLoggingService
) {

    fun logEntry(carEntry: CarEntry): Mono<CarEntry> =
        garageLoggingService.findActiveGarageLoggingBy(licensePlate = carEntry.licensePlate)
            .flatMap { logging ->
                Mono.error<CarEntry>(
                    AlreadyHasActiveGarageLoggingException(
                        message = "License plate '${carEntry.licensePlate}' has already logged in garage recently!"))
            }
            .switchIfEmpty(
                garageLoggingService.logEntry(carEntry)
                    .map { logging -> CarEntry.of(logging) })

}