package com.estapar.domain.car.park

import reactor.core.publisher.Mono

interface ParkingRepository {
    fun findEnteredByLicensePlate(licensePlate: String): Mono<Parking>
    fun save(parking: Parking): Mono<Parking>
}