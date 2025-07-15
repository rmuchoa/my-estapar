package com.estapar.infrastructure.repository

import com.estapar.domain.garage.sector.Sector
import com.estapar.domain.garage.sector.SectorRepository
import com.estapar.infrastructure.repository.postgresql.entity.GarageSectorEntity
import com.estapar.infrastructure.repository.postgresql.JPAGarageSectorRepository
import com.estapar.infrastructure.repository.postgresql.JPAGarageSpotRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class SectorRepositoryAdapter(
    val repository: JPAGarageSectorRepository,
    val spotRepository: JPAGarageSpotRepository
) : SectorRepository {

    override fun save(sector: Sector): Mono<Sector> =
        repository.save<GarageSectorEntity>(GarageSectorEntity.of(sector))
            .flatMap { entity -> fillSpots(entity) }

    override fun findById(id: Long): Mono<Sector> =
        repository.findById(id)
            .flatMap { entity -> fillSpots(entity) }

    private fun fillSpots(entity: GarageSectorEntity): Mono<Sector> =
        entity.id?.let { sectorId ->
            val sector = entity.toDomain()
            spotRepository.findBySectorId(sectorId)
                .map { spotEntity -> spotEntity.toDomain(sector) }
                .collectList()
                .map { spots -> sector.copy(spots = spots) }
        }?: Mono.empty()

}