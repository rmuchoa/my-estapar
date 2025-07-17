package com.estapar.domain.garage.spot

import com.estapar.domain.car.logging.GarageLoggingService
import com.estapar.domain.car.park.ParkingService
import com.estapar.infrastructure.api.dto.response.SpotReportResponse
import reactor.core.publisher.Mono

open class SpotReportService(
    val spotService: SpotService,
    val parkingService: ParkingService,
    val loggingService: GarageLoggingService
) {

    fun mountSpotReportFor(latitude: Double, longitude: Double): Mono<SpotReportResponse> =
        spotService.findSpotBy(latitude = latitude, longitude = longitude)
            .flatMap { spot ->
                parkingService.findEnteredBy(spotId = spot.id!!)
                    .flatMap { parking ->
                        loggingService.findActiveGarageLoggingBy(licensePlate = parking.licensePlate)
                            .map { garageLogging ->
                                SpotReportResponse.of(
                                    garageLogging = garageLogging,
                                    spot = spot)
                            }
                    }
            }

}