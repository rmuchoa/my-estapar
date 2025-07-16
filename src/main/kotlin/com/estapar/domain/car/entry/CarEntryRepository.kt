package com.estapar.domain.car.entry

import reactor.core.publisher.Mono

interface CarEntryRepository {
    fun save(carEntry: CarEntry): Mono<CarEntry>
    fun findByLicensePlate(licensePlate: String): Mono<CarEntry>
}