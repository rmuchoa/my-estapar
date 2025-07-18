package com.estapar.domain.car.park.attempt

import com.estapar.domain.car.park.Parking
import com.estapar.domain.car.park.ParkingService
import com.estapar.domain.garage.spot.SpotService
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

open class ParkAttemptService(
    val spotService: SpotService,
    val parkingService: ParkingService,
) {

    @Transactional
    open fun attemptPark(parkAttempt: ParkAttempt): Mono<Parking> =
        spotService.findSpotBy(
                latitude = parkAttempt.latitude,
                longitude = parkAttempt.longitude)
            .map { spot ->
                Parking.Companion.of(
                    parkAttempt = parkAttempt,
                    spot = spot)
            }
            .flatMap { parking ->
                parkingService.parkCarOnSpot(parking)
            }

}