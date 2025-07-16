package com.estapar.domain.car.entry

import com.estapar.domain.car.logging.GarageLogging
import com.estapar.domain.car.logging.GarageLoggingService
import reactor.core.publisher.Mono

open class CarEntryService(
    val garageLoggingService: GarageLoggingService
) {

    fun logEntry(carEntry: CarEntry): Mono<CarEntry> =
        garageLoggingService.logGarageEntry(GarageLogging.of(carEntry))
            .map { carEntry }

}