package com.estapar.domain.car.park

import com.estapar.domain.garage.spot.SpotService
import reactor.core.publisher.Mono

open class ParkAttemptService(
    val spotService: SpotService,
    val parkingService: ParkingService,
) {

    fun attemptPark(parkAttempt: ParkAttempt): Mono<Parking> =
        spotService.findSpotBy(
                latitude = parkAttempt.latitude,
                longitude = parkAttempt.longitude)
            .map { spot ->
                Parking.of(
                    parkAttempt = parkAttempt,
                    spot = spot)
            }
            .flatMap { parking ->
                parkingService.parkCarOnSpot(parking)
            }

}