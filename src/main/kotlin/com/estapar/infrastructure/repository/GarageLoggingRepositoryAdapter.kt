package com.estapar.infrastructure.repository

import com.estapar.domain.car.GarageLoggingStatus
import com.estapar.domain.car.logging.GarageLoggingRepository
import com.estapar.domain.car.logging.GarageLogging
import com.estapar.infrastructure.repository.postgresql.entity.GarageLoggingEntity
import com.estapar.infrastructure.repository.postgresql.JPAGarageLoggingRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class GarageLoggingRepositoryAdapter(
    val repository: JPAGarageLoggingRepository
) : GarageLoggingRepository {

    override fun saveGarageLogging(garageLogging: GarageLogging): Mono<GarageLogging> =
        repository.save<GarageLoggingEntity>(GarageLoggingEntity.of(garageLogging))
            .map { entity -> entity.toDomain() }

    override fun findByLicensePlate(licensePlate: String): Mono<GarageLogging> =
        repository.findByLicensePlate(licensePlate)
            .map { entity -> entity.toDomain() }

    override fun findActiveByLicensePlate(licensePlate: String): Mono<GarageLogging> =
        repository.findByLicensePlateAndStatus(
            licensePlate = licensePlate,
            status = GarageLoggingStatus.ACTIVE)
            .map { entity -> entity.toDomain() }

}