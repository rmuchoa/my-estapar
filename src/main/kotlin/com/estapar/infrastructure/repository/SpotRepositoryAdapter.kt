package com.estapar.infrastructure.repository

import com.estapar.domain.garage.spot.Spot
import com.estapar.domain.garage.spot.SpotRepository
import com.estapar.infrastructure.repository.postgresql.GarageSpotEntity
import com.estapar.infrastructure.repository.postgresql.JPAGarageSpotRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class SpotRepositoryAdapter(
    private val repository: JPAGarageSpotRepository,
    private val sectorRepository: SectorRepositoryAdapter
) : SpotRepository {

    override fun save(spot: Spot): Mono<Spot> =
        repository.save<GarageSpotEntity>(GarageSpotEntity.of(spot))
            .map { entity -> entity.toDomain(sector = spot.sector) }

    override fun findById(id: Long): Mono<Spot> =
        repository.findById(id)
            .flatMap { entity -> fillSector(entity) }

    fun buscarTodos(): Flux<Spot> =
        repository.findAll()
            .flatMap { entity -> fillSector(entity) }

    private fun fillSector(entity: GarageSpotEntity): Mono<Spot> {
        return entity.sectorId?.let { sectorId ->
            sectorRepository.findById(sectorId)
                .map { sector ->
                    entity.toDomain(sector)
                }
        }?: Mono.empty()
    }

}