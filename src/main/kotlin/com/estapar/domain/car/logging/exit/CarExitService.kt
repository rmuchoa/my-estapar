package com.estapar.domain.car.logging.exit

import com.estapar.domain.car.billing.BillingService
import com.estapar.domain.car.logging.GarageLoggingService
import com.estapar.domain.car.logging.NotFoundGarageLoggingException
import com.estapar.domain.car.park.ParkingService
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

open class CarExitService(
    val garageLoggingService: GarageLoggingService,
    val parkingService: ParkingService,
    val billingService: BillingService
) {

    @Transactional
    open fun logExit(carExit: CarExit): Mono<Void> =
        garageLoggingService.findActiveGarageLoggingBy(licensePlate = carExit.licensePlate)
            .flatMap { garageLogging ->
                Mono.zip(
                    garageLoggingService.logExit(garageLogging = garageLogging, carExit = carExit),
                    parkingService.findEnteredBy(licensePlate = carExit.licensePlate)
                        .flatMap { parking -> parkingService.unparkCarFromSpot(parking) }
                )
                    .map { it.t1 to it.t2 }
                    .flatMap { (garageLogging, parking) ->
                        billingService.chargeParking(
                            garageLogging = garageLogging,
                            parking = parking)
                    }
            }
            .switchIfEmpty(Mono.error {
                NotFoundGarageLoggingException(
                    message = "No active car logging was found for license plate: '${carExit.licensePlate}'")
            })
            .then()

}