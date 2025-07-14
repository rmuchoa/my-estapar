package com.estapar.infrastructure.repository

import com.estapar.domain.garage.sector.Sector
import com.estapar.domain.garage.sector.SectorRepository
import com.estapar.infrastructure.repository.postgresql.GarageSectorEntity
import com.estapar.infrastructure.repository.postgresql.JPAGarageSectorRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class SectorRepositoryAdapter(
    private val repository: JPAGarageSectorRepository
) : SectorRepository {

    override fun save(sector: Sector): Mono<Sector> =
        repository.save<GarageSectorEntity>(GarageSectorEntity.of(sector))
            .map { entity -> entity.toDomain() }

    override fun findById(id: Long): Mono<Sector> =
        repository.findById(id)
            .map { entity -> entity.toDomain() }

    fun buscarTodos(): Flux<Sector> =
        repository.findAll()
            .map { entity -> entity.toDomain() }

}