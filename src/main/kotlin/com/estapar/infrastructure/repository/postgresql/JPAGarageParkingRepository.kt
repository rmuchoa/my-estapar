package com.estapar.infrastructure.repository.postgresql

import com.estapar.infrastructure.repository.postgresql.entity.GarageParkingEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface JPAGarageParkingRepository : ReactiveCrudRepository<GarageParkingEntity, Long> {
    fun findByLicensePlate(licensePlate: String): Mono<GarageParkingEntity>
}