package com.estapar.domain.car.park

import com.estapar.domain.car.park.exception.NonOperationalGarageSectorException
import com.estapar.domain.car.park.exception.OverfilledOccupancyGarageSectorException
import com.estapar.domain.car.park.exception.UnavailableParkingSpotException
import com.estapar.domain.garage.sector.SectorService
import com.estapar.domain.garage.spot.SpotService
import reactor.core.publisher.Mono

open class ParkingService(
    val repository: ParkingRepository,
    val spotService: SpotService,
    val sectorService: SectorService
) {

    fun parkCarOnSpot(parking: Parking): Mono<Parking> =
        validParking(parking)
            .flatMap { valid -> repository.save(parking = valid) }
            .flatMap { saved -> markSpotAsOccupied(parking = saved) }
            .flatMap { saved -> checkSectorCapacity(parking = saved) }

    private fun validParking(parking: Parking): Mono<Parking> {
        return when {
            parking.isSpotStillOccupied() -> Mono.error {
                UnavailableParkingSpotException(
                    message = "Parking spot still remains occupied and unavailable for parking!"
                )
            }
            parking.isOutsideOpeningHours() -> Mono.error {
                NonOperationalGarageSectorException(
                    message = "Garage sector ${parking.spot.sector?.name} is out of opening hours!"
                )
            }
            parking.hasReachedSectorMaxCapacity() -> Mono.error {
                OverfilledOccupancyGarageSectorException(
                    message = "Garage sector ${parking.spot.sector?.name} has overfilled occupancy capacity!"
                )
            }
            else -> { Mono.just(parking) }
        }
    }

    private fun markSpotAsOccupied(parking: Parking): Mono<Parking> =
        spotService.saveSpot(spot = parking.spot.copy(occupied = true))
            .flatMap { spot ->
                spot.id?.let { id ->
                    spotService.findSpotBy(id)
                }?: Mono.just(spot)
            }
            .map { updatedSpot -> parking.copy(spot = updatedSpot) }

    private fun checkSectorCapacity(parking: Parking): Mono<Parking> =
        Mono.just(parking)
            .filter { parking -> parking.hasReachedSectorMaxCapacity() }
            .flatMap { parking -> sectorService.closeSectorOperation(sector = parking.spot.sector!!) }
            .map { parking }
            .switchIfEmpty(Mono.just(parking))

}