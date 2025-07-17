package com.estapar.domain.car.park.report

import com.estapar.domain.car.logging.GarageLoggingService
import com.estapar.domain.car.park.ParkingService
import reactor.core.publisher.Mono

open class ParkingReportService(
    val garageLoggingService: GarageLoggingService,
    val parkingService: ParkingService
) {

    fun mountParkingReportFor(licensePlate: String): Mono<ParkingReport> =
        Mono.zip(
            garageLoggingService.findActiveGarageLoggingBy(licensePlate),
            parkingService.findEnteredBy(licensePlate)
        ).map { it.t1 to it.t2 }
            .map { (garageLogging, parking) ->
                ParkingReport.of(
                    garageLogging = garageLogging,
                    parking = parking)
            }

}