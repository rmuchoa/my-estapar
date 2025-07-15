package com.estapar.infrastructure.repository.postgresql

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface JPAGarageSpotRepository : ReactiveCrudRepository<GarageSpotEntity, Long> {
    fun findByLatitudeAndLongitude(latitude: Double, longitude: Double): Mono<GarageSpotEntity>
    fun findBySectorId(sectorId: Long): Flux<GarageSpotEntity>
}