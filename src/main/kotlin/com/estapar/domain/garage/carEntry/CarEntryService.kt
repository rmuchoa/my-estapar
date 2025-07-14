package com.estapar.domain.garage.carEntry

import reactor.core.publisher.Mono

open class CarEntryService(
    val repository: CarEntryRepository
) {

    fun recordEntry(carEntry: CarEntry): Mono<CarEntry> =
        repository.save(carEntry)

}