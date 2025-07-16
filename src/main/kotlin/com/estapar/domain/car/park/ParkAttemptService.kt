package com.estapar.domain.car.park

import com.estapar.domain.car.entry.CarEntryService
import com.estapar.domain.garage.spot.SpotService
import reactor.core.publisher.Mono

open class ParkAttemptService(
    val spotService: SpotService,
    val parkedCarService: ParkedCarService,
    val carEntryService: CarEntryService
) {

    fun attemptPark(parkAttempt: ParkAttempt): Mono<ParkedCar> =
        Mono.zip(
            carEntryService.findCarEntryBy(
                licensePlate = parkAttempt.licensePlate),
            spotService.findSpotBy(
                latitude = parkAttempt.latitude,
                longitude = parkAttempt.longitude)
        )
            .map { tuple ->
                ParkedCar.of(
                    parkAttempt = parkAttempt,
                    carEntry = tuple.t1,
                    spot = tuple.t2)
            }
            .flatMap { parkedCar ->
                parkedCarService.parkCarOnSpot(parkedCar)
            }

}