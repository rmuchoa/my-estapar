package com.estapar.infrastructure.repository

import com.estapar.domain.garage.carEntry.CarEntry
import com.estapar.domain.garage.carEntry.CarEntryRepository
import com.estapar.infrastructure.repository.postgresql.CarEntryEntity
import com.estapar.infrastructure.repository.postgresql.JPACarEntryRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class CarEntryRepositoryAdapter(
    val repository: JPACarEntryRepository
) : CarEntryRepository {

    override fun save(carEntry: CarEntry): Mono<CarEntry> =
        repository.save<CarEntryEntity>(CarEntryEntity.of(carEntry))
            .map { entity -> entity.toDomain() }

}