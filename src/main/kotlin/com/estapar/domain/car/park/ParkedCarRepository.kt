package com.estapar.domain.car.park

import reactor.core.publisher.Mono

interface ParkedCarRepository {
    fun save(parkedCar: ParkedCar): Mono<ParkedCar>
}