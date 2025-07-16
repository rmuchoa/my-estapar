package com.estapar.infrastructure.repository

import com.estapar.domain.car.entry.CarEntry
import com.estapar.domain.car.entry.CarEntryRepository
import com.estapar.infrastructure.repository.postgresql.entity.CarEntryEntity
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

    override fun findByLicensePlate(licensePlate: String): Mono<CarEntry> =
        repository.findByLicensePlate(licensePlate)
            .map { entity -> entity.toDomain() }

}