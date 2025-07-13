package com.estapar.infrastructure.repository

import com.estapar.domain.garage.sector.Sector
import com.estapar.domain.garage.sector.SectorRepository
import com.estapar.infrastructure.repository.postgresql.GarageSectorEntity
import com.estapar.infrastructure.repository.postgresql.JPAGarageSectorRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class SectorRepositoryAdapter(
    private val repository: JPAGarageSectorRepository
) : SectorRepository {

    override fun save(sector: Sector): Mono<Sector> =
        repository.save<GarageSectorEntity>(GarageSectorEntity.of(sector))
            .map { entity -> entity.toDomain() }

}