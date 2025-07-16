package com.estapar.domain.park

import com.estapar.domain.park.exception.NonOperationalGarageSectorException
import com.estapar.domain.park.exception.OverfilledOccupancyGarageSectorException
import com.estapar.domain.park.exception.UnavailableParkingSpotException
import com.estapar.domain.garage.sector.SectorService
import com.estapar.domain.garage.spot.SpotService
import reactor.core.publisher.Mono

open class ParkedCarService(
    val repository: ParkedCarRepository,
    val spotService: SpotService,
    val sectorService: SectorService
) {

    fun parkCarOnSpot(parkedCar: ParkedCar): Mono<ParkedCar> =
        validParking(parkedCar)
            .flatMap { valid -> repository.save(parkedCar = valid) }
            .flatMap { savedCar -> markSpotAsOccupied(parkedCar = savedCar) }
            .flatMap { savedCar -> checkSectorCapacity(parkedCar = savedCar) }

    private fun validParking(parkedCar: ParkedCar): Mono<ParkedCar> {
        return when {
            parkedCar.isSpotStillOccupied() -> Mono.error {
                UnavailableParkingSpotException(
                    message = "Parking spot still remains occupied and unavailable for parking!"
                )
            }
            parkedCar.isOutsideOpeningHours() -> Mono.error {
                NonOperationalGarageSectorException(
                    message = "Garage sector ${parkedCar.spot.sector?.name} is out of opening hours!"
                )
            }
            parkedCar.hasOvercapacityAlert() -> Mono.error {
                OverfilledOccupancyGarageSectorException(
                    message = "Garage sector ${parkedCar.spot.sector?.name} has overfilled occupancy capacity!"
                )
            }
            else -> { Mono.just(parkedCar) }
        }
    }

    private fun markSpotAsOccupied(parkedCar: ParkedCar): Mono<ParkedCar> =
        spotService.saveSpot(spot = parkedCar.spot.copy(occupied = true))
            .flatMap { spot ->
                spot.id?.let { id ->
                    spotService.findSpotBy(id)
                }?: Mono.just(spot)
            }
            .map { updatedSpot -> parkedCar.copy(spot = updatedSpot) }

    private fun checkSectorCapacity(parkedCar: ParkedCar): Mono<ParkedCar> =
        Mono.just(parkedCar)
            .filter { parkedCar -> parkedCar.hasOvercapacityAlert() }
            .flatMap { parkedCar -> sectorService.closeSectorOperation(sector = parkedCar.spot.sector!!) }
            .map { parkedCar }
            .switchIfEmpty(Mono.just(parkedCar))

}