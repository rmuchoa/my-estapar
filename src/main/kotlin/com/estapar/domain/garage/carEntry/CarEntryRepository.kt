package com.estapar.domain.garage.carEntry

import reactor.core.publisher.Mono

interface CarEntryRepository {
    fun save(carEntry: CarEntry): Mono<CarEntry>
}