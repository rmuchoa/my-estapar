package com.estapar.domain.garage.spot

import reactor.core.publisher.Mono

interface SpotRepository {
    fun save(spot: Spot): Mono<Spot>
    fun findById(id: Long): Mono<Spot>
    fun findByGeoposition(latitude: Double, longitude: Double): Mono<Spot>
}