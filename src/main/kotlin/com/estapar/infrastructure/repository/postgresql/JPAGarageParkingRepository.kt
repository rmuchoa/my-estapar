package com.estapar.infrastructure.repository.postgresql

import com.estapar.domain.car.park.ParkingStatus
import com.estapar.infrastructure.repository.postgresql.entity.GarageParkingEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface JPAGarageParkingRepository : ReactiveCrudRepository<GarageParkingEntity, Long> {
    fun findByLicensePlateAndStatus(licensePlate: String, status: ParkingStatus): Mono<GarageParkingEntity>
}