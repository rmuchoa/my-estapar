package com.estapar.domain.carEntry

import reactor.core.publisher.Mono

interface CarEntryRepository {
    fun save(carEntry: CarEntry): Mono<CarEntry>
    fun findByLicensePlate(licensePlate: String): Mono<CarEntry>
}