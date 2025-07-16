package com.estapar.infrastructure.repository

import com.estapar.domain.car.park.ParkedCar
import com.estapar.domain.car.park.ParkedCarRepository
import com.estapar.infrastructure.repository.postgresql.JPAParkedCarRepository
import com.estapar.infrastructure.repository.postgresql.entity.ParkedCarEntity
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class ParkedCarRepositoryAdapter(
    val repository: JPAParkedCarRepository
) : ParkedCarRepository {

    override fun save(parkedCar: ParkedCar): Mono<ParkedCar> =
        repository.save<ParkedCarEntity>(ParkedCarEntity.of(parkedCar))
            .map { entity ->
                entity.toDomain(
                    spot = parkedCar.spot,
                    carEntry = parkedCar.carEntry)
            }

}