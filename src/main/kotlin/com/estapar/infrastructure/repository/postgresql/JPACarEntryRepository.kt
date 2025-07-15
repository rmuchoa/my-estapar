package com.estapar.infrastructure.repository.postgresql

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface JPACarEntryRepository : ReactiveCrudRepository<CarEntryEntity, Long> {
    fun findByLicensePlate(licensePlate: String): Mono<CarEntryEntity>
}