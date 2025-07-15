package com.estapar.infrastructure.repository

import com.estapar.domain.garage.spot.Spot
import com.estapar.domain.garage.spot.SpotRepository
import com.estapar.infrastructure.repository.postgresql.entity.GarageSpotEntity
import com.estapar.infrastructure.repository.postgresql.JPAGarageSectorRepository
import com.estapar.infrastructure.repository.postgresql.JPAGarageSpotRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class SpotRepositoryAdapter(
    private val repository: JPAGarageSpotRepository,
    private val sectorRepository: JPAGarageSectorRepository
) : SpotRepository {

    override fun save(spot: Spot): Mono<Spot> =
        repository.save<GarageSpotEntity>(GarageSpotEntity.of(spot))
            .map { entity -> entity.toDomain(sector = spot.sector) }

    override fun findById(id: Long): Mono<Spot> =
        repository.findById(id)
            .flatMap { entity -> fillSector(entity) }

    override fun findByGeoposition(latitude: Double, longitude: Double): Mono<Spot> =
        repository.findByLatitudeAndLongitude(latitude, longitude)
            .flatMap { entity -> fillSector(entity) }

    private fun fillSector(entity: GarageSpotEntity): Mono<Spot> {
        return entity.sectorId?.let { sectorId ->
            sectorRepository.findById(sectorId)
                .map { sectorEntity ->
                    entity.toDomain(sector = sectorEntity.toDomain())
                }
        }?: Mono.empty()
    }

}