package com.estapar.infrastructure.repository

import com.estapar.domain.car.park.Parking
import com.estapar.domain.car.park.ParkingRepository
import com.estapar.infrastructure.repository.postgresql.JPAGarageParkingRepository
import com.estapar.infrastructure.repository.postgresql.entity.GarageParkingEntity
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class ParkingRepositoryAdapter(
    val repository: JPAGarageParkingRepository
) : ParkingRepository {

    override fun save(parking: Parking): Mono<Parking> =
        repository.save<GarageParkingEntity>(GarageParkingEntity.of(parking))
            .map { entity -> entity.toDomain(spot = parking.spot) }

}