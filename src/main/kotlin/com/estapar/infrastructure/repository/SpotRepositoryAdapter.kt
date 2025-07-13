package com.estapar.infrastructure.repository

import com.estapar.domain.garage.spot.Spot
import com.estapar.domain.garage.spot.SpotRepository
import com.estapar.infrastructure.repository.postgresql.GarageSpotEntity
import com.estapar.infrastructure.repository.postgresql.JPAGarageSpotRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class SpotRepositoryAdapter(
    private val repository: JPAGarageSpotRepository
) : SpotRepository {

    override fun save(spot: Spot): Mono<Spot> =
        repository.save<GarageSpotEntity>(GarageSpotEntity.of(spot))
            .map { entity -> entity.toDomain(sector = spot.sector) }

}