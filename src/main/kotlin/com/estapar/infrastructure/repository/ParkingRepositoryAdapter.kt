package com.estapar.infrastructure.repository

import com.estapar.domain.car.park.Parking
import com.estapar.domain.car.park.ParkingRepository
import com.estapar.domain.garage.spot.SpotRepository
import com.estapar.infrastructure.repository.postgresql.JPAGarageParkingRepository
import com.estapar.infrastructure.repository.postgresql.entity.GarageParkingEntity
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class ParkingRepositoryAdapter(
    val repository: JPAGarageParkingRepository,
    val spotRepository: SpotRepository
) : ParkingRepository {

    override fun save(parking: Parking): Mono<Parking> =
        repository.save<GarageParkingEntity>(GarageParkingEntity.of(parking))
            .map { entity -> entity.toDomain(spot = parking.spot) }

    override fun findByLicensePlate(licensePlate: String): Mono<Parking> =
        repository.findByLicensePlate(licensePlate)
            .flatMap { entity -> fillSpot(entity) }

    private fun fillSpot(entity: GarageParkingEntity): Mono<Parking> =
        spotRepository.findById(id = entity.spotId)
            .map { spot -> entity.toDomain(spot) }

}