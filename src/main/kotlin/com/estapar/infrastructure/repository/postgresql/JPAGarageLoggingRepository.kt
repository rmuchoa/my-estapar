package com.estapar.infrastructure.repository.postgresql

import com.estapar.domain.car.GarageLoggingStatus
import com.estapar.infrastructure.repository.postgresql.entity.GarageLoggingEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface JPAGarageLoggingRepository : ReactiveCrudRepository<GarageLoggingEntity, Long> {
    fun findByLicensePlate(licensePlate: String): Mono<GarageLoggingEntity>
    fun findByLicensePlateAndStatus(licensePlate: String, status: GarageLoggingStatus): Mono<GarageLoggingEntity>
}