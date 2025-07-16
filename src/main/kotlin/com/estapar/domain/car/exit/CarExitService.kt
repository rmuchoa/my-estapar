package com.estapar.domain.car.exit

import com.estapar.domain.car.logging.GarageLoggingService
import com.estapar.domain.car.logging.NotFoundGarageLoggingException
import com.estapar.domain.car.park.ParkingService
import reactor.core.publisher.Mono

open class CarExitService(
    val garageLoggingService: GarageLoggingService,
    val parkingService: ParkingService
) {

    fun logExit(carExit: CarExit): Mono<Void> =
        garageLoggingService.findActiveGarageLoggingBy(licensePlate = carExit.licensePlate)
            .flatMap { garageLogging -> garageLoggingService.logExit(garageLogging) }
            .flatMap { garageLogging -> parkingService.findBy(licensePlate = garageLogging.licensePlate) }
            .flatMap { parking -> parkingService.unparkCarFromSpot(parking) }
            .switchIfEmpty(Mono.error {
                NotFoundGarageLoggingException(
                    message = "No active car logging was found for license plate: '${carExit.licensePlate}'")
            })
            .then()

}