package com.estapar.domain.garage.park

import reactor.core.publisher.Mono

interface ParkedCarRepository {
    fun save(parkedCar: ParkedCar): Mono<ParkedCar>
}