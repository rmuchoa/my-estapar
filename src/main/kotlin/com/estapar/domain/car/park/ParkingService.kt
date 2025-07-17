package com.estapar.domain.car.park

import com.estapar.domain.car.park.exception.NonOperationalGarageSectorException
import com.estapar.domain.car.park.exception.OverfilledOccupancyGarageSectorException
import com.estapar.domain.car.park.exception.UnavailableParkingSpotException
import com.estapar.domain.garage.sector.SectorService
import com.estapar.domain.garage.spot.SpotService
import reactor.core.publisher.Mono
import java.time.LocalDateTime

open class ParkingService(
    val repository: ParkingRepository,
    val spotService: SpotService,
    val sectorService: SectorService
) {

    fun findEnteredBy(licensePlate: String): Mono<Parking> =
        repository.findEnteredByLicensePlate(licensePlate)

    fun findEnteredBy(spotId: Long): Mono<Parking> =
        repository.findEnteredBySpotId(spotId)

    fun parkCarOnSpot(parking: Parking): Mono<Parking> =
        validParking(parking)
            .flatMap { valid -> repository.save(parking = valid) }
            .flatMap { saved -> markSpotAsOccupied(parking = saved) }
            .flatMap { saved -> checkSectorCapacityToClose(parking = saved) }

    fun unparkCarFromSpot(parking: Parking): Mono<Parking> =
        repository.save(
            parking = parking.copy(
                unparkingTime = LocalDateTime.now(),
                status = ParkingStatus.LEAVED))
            .flatMap { saved -> markSpotAsFree(parking = saved) }
            .flatMap { saved -> checkSectorCapacityToReopen(parking = saved) }

    private fun validParking(parking: Parking): Mono<Parking> =
        when {
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

    private fun markSpotAsOccupied(parking: Parking): Mono<Parking> =
        spotService.saveSpot(spot = parking.spot.copy(occupied = true))
            .flatMap { spot ->
                spot.id!!.let { id -> spotService.findSpotBy(id) }
            }
            .map { updatedSpot -> parking.copy(spot = updatedSpot) }

    private fun markSpotAsFree(parking: Parking): Mono<Parking> =
        spotService.saveSpot(spot = parking.spot.copy(occupied = false))
            .flatMap { spot ->
                spot.id!!.let { id -> spotService.findSpotBy(id) }
            }
            .map { updatedSpot -> parking.copy(spot = updatedSpot) }

    private fun checkSectorCapacityToClose(parking: Parking): Mono<Parking> =
        Mono.just(parking)
            .filter { parking -> parking.hasReachedSectorMaxCapacity() }
            .flatMap { parking ->
                sectorService.closeSectorOperation(sector = parking.spot.sector!!)
            }
            .map { parking }
            .switchIfEmpty(Mono.just(parking))

    private fun checkSectorCapacityToReopen(parking: Parking): Mono<Parking> =
        Mono.just(parking)
            .filter { parking -> parking.hasReachedSectorMaxCapacity() }
            .switchIfEmpty(
                sectorService.reopenSectorOperation(sector = parking.spot.sector!!)
                    .map { parking })

}