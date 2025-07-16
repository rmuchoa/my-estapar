package com.estapar.domain.car.entry

import reactor.core.publisher.Mono

open class CarEntryService(
    val repository: CarEntryRepository
) {

    fun recordEntry(carEntry: CarEntry): Mono<CarEntry> =
        repository.save(carEntry)

    fun findCarEntryBy(licensePlate: String): Mono<CarEntry> =
        repository.findByLicensePlate(licensePlate)
            .switchIfEmpty(Mono.error {
                NotFoundCarEntryException(
                    message = "No one car entry was found for license plate: '$licensePlate'")
            })

}