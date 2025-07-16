package com.estapar.domain.car.park

import reactor.core.publisher.Mono

interface ParkingRepository {
    fun save(parking: Parking): Mono<Parking>
}